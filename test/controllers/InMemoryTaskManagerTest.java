package controllers;


import controllers.generallogicfortasks.Managers;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    public void init() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        super.init();
    }

}

