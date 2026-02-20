package ai.qodo.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DataAccessApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should load successfully");
    }

    @Test
    void testAllDaoBeans_AreCreated() {
        assertNotNull(applicationContext.getBean(StockDao.class), "StockDao bean should be created");
        assertNotNull(applicationContext.getBean(OrderDao.class), "OrderDao bean should be created");
        assertNotNull(applicationContext.getBean(TradeDao.class), "TradeDao bean should be created");
        assertNotNull(applicationContext.getBean(UserProfileDao.class), "UserProfileDao bean should be created");
    }

    @Test
    void testTableCreatorBean_IsCreated() {
        assertNotNull(applicationContext.getBean(TableCreator.class), "TableCreator bean should be created");
    }

    @Test
    void testConfigurationBean_IsCreated() {
        assertNotNull(applicationContext.getBean(Configuration.class), "Configuration bean should be created");
    }
}
