package ru.otus.aop;

import org.objectweb.asm.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class MethodAnnotationScanner extends MethodVisitor {
    private boolean hasLogAnnotation = false;
    ArrayList<String> parameterIndexes;
    String methodName;
    public MethodAnnotationScanner(MethodVisitor mv, String name) {
        super(Opcodes.ASM5, mv);
        parameterIndexes = new ArrayList<>();
        methodName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if ("Lru/otus/annotations/Log;".equals(desc)) {
            hasLogAnnotation = true;
            return new AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                public AnnotationVisitor visitArray(String name) {
                    if ("fields".equals(name)) {
                        return new AnnotationVisitor(Opcodes.ASM5, super.visitArray(name)) {
                            public void visit(String name, Object value) {
                                parameterIndexes.add((String) value);
                                super.visit(name, value);
                            }
                        };
                    } else {
                        return super.visitArray(name);
                    }
                }
            };
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (hasLogAnnotation) {
            for (String index : parameterIndexes) {
                var paramNumber = Integer.valueOf(index);
                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(Opcodes.ILOAD,  paramNumber+ 1);

                var handle = new Handle(
                        H_INVOKESTATIC,
                        Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                        "makeConcatWithConstants",
                        MethodType.methodType(CallSite.class,
                                MethodHandles.Lookup.class,
                                String.class,
                                MethodType.class,
                                String.class,
                                Object[].class).toMethodDescriptorString(),
                        false);
                mv.visitInvokeDynamicInsn("makeConcatWithConstants",
                        "(I)Ljava/lang/String;",
                        handle,
                        "logged call: "+methodName+"["+paramNumber+"]:\u0001");

                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
        }
    }
}