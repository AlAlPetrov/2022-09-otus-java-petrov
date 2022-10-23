package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

class SampleTest {
    public SampleTest(){
    }

    @Test
    void SucceededTest() {
    }

    @Test
    void FailedTest() throws Exception {
        throw new Exception("assertion failed");
    }

    @Before
    void Init() throws Exception {
    }

    @After
    void Teardown() {
    }
}