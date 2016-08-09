package com.cybervision.market.service.impl;

import com.cybervision.market.dao.CustomItemDao;
import com.cybervision.market.dao.GenericDao;
import com.cybervision.market.dao.CustomUserDao;
import com.cybervision.market.entity.*;
import com.cybervision.market.exception.ItemNotFoundException;
import com.cybervision.market.exception.NotEnoughMoneyException;
import com.cybervision.market.service.BasketManagerService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BasketManagerServiceImpl implements BasketManagerService {

    @Autowired
    private GenericDao<Vendor, Long> vendorDao;
    @Autowired
    private GenericDao<Basket, Long> basketDao;
    @Autowired
    private GenericDao<Supply, Long> supplyDao;

    //    @Autowired
//    private GenericDao<User> userDao;
    @Autowired
    private CustomUserDao userDao;
    @Autowired
    private CustomItemDao customItemDao;


    @Override
    public Long saveItem(Item item) {
        return customItemDao.save(item);
    }

    @Override
    public void updateItem(Item item) throws NotEnoughMoneyException {
        customItemDao.update(item);
    }

    @Override
    public Long saveBasket(Basket basket) throws NotEnoughMoneyException {
        return basketDao.save(basket);
    }

    @Override
    public void updateBasket(Basket basket) throws NotEnoughMoneyException {
        basketDao.update(basket);
    }

    @Override
    public Set<Basket> getAllBasketsFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException {
        return userDao.get(user.getId()).getBaskets();
    }

    @Override
    public Set<Basket> getBasketsAmountFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException {
        return null;
    }

    @Override
    public void saveSupply(Supply supply) {
        supplyDao.save(supply);
    }

    @Override
    public void updateSupply(Supply supply) throws NotEnoughMoneyException {
        supplyDao.update(supply);
    }

    @Override
    public Set<Supply> getAllSuppliesFromVendor(Vendor vendor) {
        return vendorDao.get(vendor.getId()).getSupplies();
    }

    @Override
    public Set<Basket> getSuppliesAmountFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException {
        return null;
    }


    @Override
    public void addItemByIdToBasket(Long id, Basket basket, int quantity) throws NotEnoughMoneyException, ItemNotFoundException {
        Item item = customItemDao.get(id);
        if (item == null) {
            throw new ItemNotFoundException(id);
        }
        Optional<ItemOrder> existedItemOrder = basket.getItemOrders().stream().filter(o -> item.equals(o.getItem())).findFirst();
        if (existedItemOrder.isPresent()) {
            existedItemOrder.get().setQuantity(existedItemOrder.get().getQuantity() + quantity);
            basketDao.update(basket);
        } else {
            basket.getItemOrders().add(
                    new ItemOrder(item, basket, quantity)
            );
            basketDao.update(basket);
        }
    }

    @Override
    public void addItemByIdToSupply(long id, Supply supply, int quantity) {
        Item item = customItemDao.get(id);
        if (item == null) {
            throw new ItemNotFoundException(id);
        }
        Optional<SupplyList> existedSupplyList = supply.getSupplyList().stream().filter(o -> item.equals(o.getItem())).findFirst();
        if (existedSupplyList.isPresent()) {
            existedSupplyList.get().setQuantity(existedSupplyList.get().getQuantity() + quantity);
            supplyDao.update(supply);
        } else {
            supply.getSupplyList().add(
                    new SupplyList(item, supply, quantity)
            );
            supplyDao.update(supply);
        }
    }

    @Override
    public Long saveVendor(Vendor vendor) {
        return vendorDao.save(vendor);
    }

    @Override
    public Vendor getVendor(Long id) {
        return vendorDao.get(id);
    }

    @Override
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }


    @Override
    public User getUser(Long id) {
        User user = userDao.get(id);
        Hibernate.initialize(user.getBaskets());
        return user;
    }

    @Override
    public void buyBasket(Basket basket) throws NotEnoughMoneyException {
        if (basket.getUser().getAvailableMoney() >= basket.calculateSummaryCost()) {
            basket.setBought(true);
            basketDao.update(basket);
        } else {
            throw new NotEnoughMoneyException(basket.getUser().getAvailableMoney(), basket.calculateSummaryCost());
        }
    }

    @Override
    public Set<Item> getAllItemsFromBasket(Basket basket) throws NotEnoughMoneyException, ItemNotFoundException {
        return basketDao.get(basket.getId()).getItems();
    }

    @Override
    public Double calculateSummaryCost(Basket basket) {
        Basket tmpBasket = basketDao.get(basket.getId());
        return tmpBasket.calculateSummaryCost();
    }

    @Override
    public Basket getBasket(Long id) {
        return basketDao.get(id);
    }

    @Override
    public void deleteBasket(Basket basket) {
        basketDao.delete(basket);
    }

    @Override
    public void clearDatabase() {
        customItemDao.removeAll();
        supplyDao.removeAll();
        basketDao.removeAll();
        vendorDao.removeAll();
        userDao.removeAll();
    }

    @Override
    public List<Item> getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyByVendor(Vendor vendor) {
        return customItemDao.getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyBy(vendor);
    }

}
