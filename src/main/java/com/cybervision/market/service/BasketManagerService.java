package com.cybervision.market.service;

import com.cybervision.market.entity.*;
import com.cybervision.market.exception.ItemNotFoundException;
import com.cybervision.market.exception.NotEnoughMoneyException;

import java.util.List;
import java.util.Set;

public interface BasketManagerService {

    Long saveItem(Item item) throws NotEnoughMoneyException;

    void updateItem(Item item) throws NotEnoughMoneyException;

    Set<Item> getAllItemsFromBasket(Basket basket) throws NotEnoughMoneyException, ItemNotFoundException;


    Long saveBasket(Basket basket) throws NotEnoughMoneyException;

    void updateBasket(Basket basket) throws NotEnoughMoneyException;

    Set<Basket> getAllBasketsFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException;

    Set<Basket> getBasketsAmountFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException;


    void saveSupply(Supply supply);

    void updateSupply(Supply supply) throws NotEnoughMoneyException;

    Set<Supply> getAllSuppliesFromVendor(Vendor vendor);

    Set<Basket> getSuppliesAmountFromUser(User user) throws NotEnoughMoneyException, ItemNotFoundException;


    void addItemByIdToBasket(Long id, Basket basket, int quantity) throws NotEnoughMoneyException, ItemNotFoundException;

    void addItemByIdToSupply(long id, Supply supply, int quantity);



    Long saveVendor(Vendor vendor);

    Vendor getVendor(Long id);


    void saveUser(User user);

    void deleteUser(User user);

    User getUserByEmail(String email);

    User getUser(Long id);


    void buyBasket(Basket basket) throws NotEnoughMoneyException;

    Double calculateSummaryCost(Basket basket);

    Basket getBasket(Long id);

    void deleteBasket(Basket basket);

    void clearDatabase();


    List<Item> getItemsDistinctWithMoreThenOneSupplyWithMoreThenTwoQuantityInEachSupplyByVendor(Vendor vendor);

}
