package controllers;


import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void init() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        super.init();
    }

}

