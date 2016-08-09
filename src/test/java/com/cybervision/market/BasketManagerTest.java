package com.cybervision.market;

import com.cybervision.market.entity.*;
import com.cybervision.market.exception.ItemNotFoundException;
import com.cybervision.market.exception.NotEnoughMoneyException;
import com.cybervision.market.service.BasketManagerService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MarketApplication.class)
@Transactional//(propagation = Propagation.REQUIRED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BasketManagerTest extends Assert {

    /*

    todo ask
    Вопросы!
    4. @Transactional в тесте делает ролбек, но тогда получается вложенна транзация. Убрать @Transactional ? Пытался через @Rollback.
    5. Нормально ли настроил HibernateConfiguration ? Как заменить SessionFactory ?
    6. Lazy. Только в той же сессии? То есть нужно знать заранее какие коллекции понадобятся...

     */

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;
    private Item item5;
    private Item item6;
    private Item item7;
    private Supply supply;
    private Supply supply2;
    private Vendor vendor;
    private Vendor vendor2;

    @Autowired
    private Basket basket;

    @Autowired
    private BasketManagerService basketManager;


    @Configuration
    static class BasketManagerTestContextConfiguration {
        @Bean
        public Basket basket() {
            return new Basket("The first basket", new User("beltseraa@gmail.com", "qweqwe", 500d), new Timestamp(new Date().getTime()));
        }
    }

    //    @BeforeTransaction
    @Before
    public void setUp() throws Exception {
        item1 = new Item("pen", 20d);
        item2 = new Item("pencil", 20d);
        item3 = new Item("ball", 20d);
        item4 = new Item("dol", 20d);
        item5 = new Item("apple", 20d);
        item6 = new Item("toy car", 200d);
        item7 = new Item("toy airplane", 300d);
        vendor = new Vendor("Roshen", "verhovna rada");
        vendor2 = new Vendor("Prosokvash", "selo Prosokvasheno");
        supply = new Supply(vendor, Timestamp.valueOf(LocalDateTime.now()));
        supply2 = new Supply(vendor, Timestamp.valueOf(LocalDateTime.now()));
        basketManager.clearDatabase();
    }

    @Test
    public void testSupplyFlow() throws Exception {
        long ids[] = new long[5];
        ids[0] = basketManager.saveItem(item1);
        ids[1] = basketManager.saveItem(item2);
        ids[2] = basketManager.saveItem(item3);
        ids[3] = basketManager.saveItem(item4);
        ids[4] = basketManager.saveItem(item5);
        basketManager.saveVendor(vendor);
        basketManager.saveVendor(vendor2);
        basketManager.saveSupply(supply);
        basketManager.saveSupply(supply2);
        basketManager.addItemByIdToSupply(ids[0], supply, 2);
        basketManager.addItemByIdToSupply(ids[1], supply, 2);
        basketManager.addItemByIdToSupply(ids[3], supply, 2);

        basketManager.addItemByIdToSupply(ids[0], supply2, 2);
        basketManager.addItemByIdToSupply(ids[1], supply2, 2);
        basketManager.addItemByIdToSupply(ids[2], supply2, 2);

        int wrongItemId = -2910212;
        expectedException.expect(ItemNotFoundException.class);
        basketManager.addItemByIdToSupply(wrongItemId, supply, 2);
    }

    @Test
    public void testSaveBasketCostLimitExceed() throws Exception {
        basketManager.saveUser(basket.getUser());
        Long id = basketManager.saveItem(item1);
        Long id2 = basketManager.saveItem(item2);
        Long id6 = basketManager.saveItem(item6);
        Long id7 = basketManager.saveItem(item7);
        basketManager.saveBasket(basket);
        basketManager.addItemByIdToBasket(id, basket, 2);
        basketManager.addItemByIdToBasket(id, basket, 5);
        basketManager.addItemByIdToBasket(id2, basket, 1);
        basketManager.addItemByIdToBasket(id6, basket, 1);
        basketManager.buyBasket(basket);
        basketManager.addItemByIdToBasket(id7, basket, 1);
        expectedException.expect(NotEnoughMoneyException.class);
        basketManager.buyBasket(basket);
    }

    @Test
    public void testCalculateSummaryCost() throws Exception {
        long ids[] = new long[5];
        basketManager.saveUser(basket.getUser());
        basketManager.saveBasket(basket);
        ids[0] = basketManager.saveItem(item1);
        ids[1] = basketManager.saveItem(item2);
        ids[2] = basketManager.saveItem(item3);
        ids[3] = basketManager.saveItem(item4);
        ids[4] = basketManager.saveItem(item5);
        for (long id : ids) {
            basketManager.addItemByIdToBasket(id, basket, 2);
        }
        Double basketCost = basketManager.calculateSummaryCost(basket);

        Double expectedResult = 2 * (item1.getCost() + item2.getCost() + item3.getCost() + item4.getCost() + item5.getCost());
        assertEquals(expectedResult, basketCost);
    }

    @Test
    public void testAddItemToBasketTwice() throws Exception {
        basketManager.saveItem(item2);
        Long id = basketManager.saveItem(item1);
        Long id3 = basketManager.saveItem(item3);
        basketManager.saveBasket(basket);
        basketManager.addItemByIdToBasket(id, basket, 2);

        basket = basketManager.getBasket(basket.getId());
        assertNotNull(basket);

        basketManager.addItemByIdToBasket(id3, basket, 3);
        basketManager.addItemByIdToBasket(id, basket, 4);

        basket = basketManager.getBasket(this.basket.getId());
        assertEquals(2, basket.getItemOrders().size());
        int itemsQuantity = basket.getItemOrders().stream().reduce(
                0,
                (s, i) -> s + i.getQuantity(),
                (s1, s2) -> s1 + s2
        );
        assertEquals(9, itemsQuantity);
    }


    @Ignore
    @Test
    public void testComplexQuery() throws Exception {
        item1.getSupplyList().add(new SupplyList(item1, supply, 200));
        item2.getSupplyList().add(new SupplyList(item2, supply, 20));
        item3.getSupplyList().add(new SupplyList(item3, supply, 200));
        item4.getSupplyList().add(new SupplyList(item4, supply, 20));

        item1.getSupplyList().add(new SupplyList(item1, supply2, 200));
        item2.getSupplyList().add(new SupplyList(item2, supply2, 200));
        item3.getSupplyList().add(new SupplyList(item3, supply2, 200));
        basketManager.saveVendor(vendor);
        basketManager.saveVendor(vendor2);
        basketManager.saveSupply(supply);
        basketManager.saveSupply(supply2);
        basketManager.saveItem(item1);
        basketManager.saveItem(item2);
        basketManager.saveItem(item3);
        basketManager.saveItem(item4);

        List<Item> resultItems = basketManager.getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyByVendor(vendor);
        System.out.println("=====================");
        resultItems.stream().forEach(i -> System.out.println("Item: " + i));
        System.out.println("=====================");
        assertEquals(item1.getTitle(), resultItems.get(0).getTitle());
        assertEquals(item3.getTitle(), resultItems.get(1).getTitle());
    }


    @Ignore
    @Test
    public void testDeleteBasket() throws Exception {
        basketManager.saveBasket(basket);
        Long id1 = basketManager.saveItem(item1);
        Long id2 = basketManager.saveItem(item2);
        Long id3 = basketManager.saveItem(item3);
        basketManager.addItemByIdToBasket(id1, basket, 1);
        basketManager.addItemByIdToBasket(id2, basket, 1);
        basketManager.addItemByIdToBasket(id3, basket, 1);
        basketManager.deleteBasket(basket);
    }

    @Ignore
    @Test
    public void testSaveNew() throws Exception {
        User user = basket.getUser();
        basketManager.saveUser(user);
        basketManager.saveBasket(basket);
        User user2 = basketManager.getUser(user.getId());
        assertEquals(1, user2.getBaskets().size());
    }

    @Ignore
    @Test
    public void testGetUserByEmail() throws Exception {
        final String email = "serg29@gmail.com";
        basketManager.saveUser(new User(email, "lalala", 200d));
        User userByEmail = basketManager.getUserByEmail(email);
        assertNotNull(userByEmail);
    }

    @Ignore
    @Test
    public void testUpdateBasket() throws Exception {
        basketManager.saveBasket(basket);
        basket.setTitle("CHANGED TITLE 2");
        basketManager.updateBasket(basket);
        Basket basket2 = basketManager.getBasket(this.basket.getId());
        assertEquals(basket2.getTitle(), basket.getTitle());
    }

    @Ignore
    @Test
    public void testAddItemByIdToBasket() throws Exception {
        Long itemId1 = basketManager.saveItem(item1);
        Long itemId2 = basketManager.saveItem(item2);
        Long itemId3 = basketManager.saveItem(item3);
        basketManager.saveBasket(basket);
        basketManager.addItemByIdToBasket(itemId1, basket, 1);
        basketManager.addItemByIdToBasket(itemId2, basket, 2);
        basketManager.addItemByIdToBasket(itemId3, basket, 3);
        basketManager.addItemByIdToBasket(itemId2, basket, 5);
    }

    @Ignore
    @Test
    public void testAddItemByWrongIdToBasket() throws Exception {
        Long itemId1 = basketManager.saveItem(item1);
        Random random = new Random();
        Long wrongId = 10_000 + random.nextLong();
        basketManager.addItemByIdToBasket(itemId1, basket, 1);
        expectedException.expect(ItemNotFoundException.class);
        basketManager.addItemByIdToBasket(wrongId, basket, 1);
        basketManager.saveBasket(basket);
    }
}
