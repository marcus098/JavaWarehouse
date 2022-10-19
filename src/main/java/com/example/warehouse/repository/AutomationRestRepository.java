package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOAutomation;
import com.example.warehouse.DAO.DAOOrder;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Automation;
import com.example.warehouse.model.DiscountRules;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.StocksRules;
import com.example.warehouse.service.AutomationRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AutomationRestRepository {
    @Autowired
    private DAOAutomation daoAutomation;
    @Autowired
    private DataSource dataSource;
    private List<Automation> automationList;

    public List<Automation> loadAutomations() {
        return automationList;
    }

    public List<Automation> getAutomations() {
        Connection connection = null;
        connection = dataSource.getConnection();
        List<Automation> automations = new ArrayList<>();
        try {
            automations = DAOAutomation.getInstance().getAutomations(connection);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(automations);
        automationList = automations;
        return automations;
    }

    public ReturnWithMessage addDiscountRule(DiscountRules discountRules) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            ReturnWithMessage r = DAOAutomation.getInstance().insertDiscountRule(connection, discountRules);
            if (r.isBool()) {
                Optional<DiscountRules> optionalDiscountRules = automationList.get(0).getDiscountRulesList().stream().filter(discountRules1 -> discountRules1.getPeriod() == discountRules.getPeriod() && discountRules.getTypePeriod() == discountRules1.getTypePeriod() && discountRules.getPercentage() == discountRules1.getPercentage()).findFirst();
                if (optionalDiscountRules.isEmpty()) {
                    discountRules.setId((long) r.getObject());
                    boolean bool = automationList.get(0).getDiscountRulesList().add(discountRules);
                    if (bool)
                        return new ReturnWithMessage(true, "Aggiunto con Successo", r.getObject());
                    return new ReturnWithMessage(false, "Errore");
                } else {
                    return new ReturnWithMessage(false, "Regola già presente");
                }
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage addStockRule(StocksRules stocksRules) {
        return modifyStockRule(stocksRules.getNumber(), stocksRules.getId());
    }

    public ReturnWithMessage modifyStockRule(long number, long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            if (DAOAutomation.getInstance().modifyStocksRules(connection, number, id).isBool()) {
                Optional<StocksRules> optionalStocksRules = automationList.get(1).getStocksRulesList().stream().filter(stocksRules -> stocksRules.getId() == id).findFirst();
                if (optionalStocksRules.isEmpty())
                    return new ReturnWithMessage(false, "Errore");
                optionalStocksRules.get().setNumber(number);
                return new ReturnWithMessage(true, "Modificato con successo");
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage modifyDiscountRule(DiscountRules discountRules) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            if (DAOAutomation.getInstance().modifyDiscountRules(connection, discountRules).isBool()) {
                Optional<DiscountRules> optionalDiscountRules = automationList.get(0).getDiscountRulesList().stream().filter(discountRules1 -> discountRules1.getId() == discountRules.getId()).findFirst();
                if (optionalDiscountRules.isEmpty())
                    return new ReturnWithMessage(false, "Errore");
                optionalDiscountRules.get().setPercentage(discountRules.getPercentage());
                optionalDiscountRules.get().setPeriod(discountRules.getPeriod());
                optionalDiscountRules.get().setTypePeriod(discountRules.getTypePeriod());
                return new ReturnWithMessage(true, "Modificato con successo");
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage removeStockRule(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            if (DAOAutomation.getInstance().modifyStocksRules(connection, -1, id).isBool()) {
                Optional<StocksRules> optionalStocksRules = automationList.get(1).getStocksRulesList().stream().filter(stocksRules -> stocksRules.getId() == id).findFirst();
                if (optionalStocksRules.isEmpty())
                    return new ReturnWithMessage(false, "Errore");
                optionalStocksRules.get().setNumber(-1);
                return new ReturnWithMessage(true, "Modificato con successo");
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage removeDiscountRule(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        System.out.println(automationList);
        try {
            if (DAOAutomation.getInstance().deleteDiscountRule(connection, id).isBool()) {
                for (int i = 0; i < automationList.get(0).getDiscountRulesList().size(); i++) {
                    if (automationList.get(0).getDiscountRulesList().get(i).getId() == id) {
                        automationList.get(0).getDiscountRulesList().remove(i);
                        return new ReturnWithMessage(true, "Eliminata con successo");
                    }
                }

            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage activeAutomation(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            if (DAOAutomation.getInstance().modify(connection, true, id).isBool()) {
                for (Automation auto : automationList) {
                    if (auto.getId() == id) {
                        auto.setActive(true);
                        return new ReturnWithMessage(true, "Abilitata con successo");
                    }
                }
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public ReturnWithMessage disableAutomation(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            if (DAOAutomation.getInstance().modify(connection, false, id).isBool()) {
                for (Automation auto : automationList) {
                    if (auto.getId() == id) {
                        auto.setActive(false);
                        return new ReturnWithMessage(true, "Disabilitata con successo");
                    }
                }
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
        return new ReturnWithMessage(false, "Errore");
    }

    public double DiscountManagement(long productId) {
        double discount = 0;
        Connection connection = null;
        connection = dataSource.getConnection();
        //valori impostati dall'utente
        if (automationList.get(0).isActive()) {
            int daysInterval = 0;
            //database per intervallo giorni
            try {
                daysInterval = daoAutomation.getIntervalSell(connection, productId);
            } catch (DAOException e) {
                return 0;
            }
            //fine
            System.out.println(daysInterval);
            if (automationList.get(0).getDiscountRulesList().size() != 0) {//impostazioni settate dall'utente
                int max = -1;
                int maxAll = -1;
                for (DiscountRules rule : automationList.get(0).getDiscountRulesList()) {
                    int period = rule.getPeriod();
                    if (rule.getTypePeriod() == 1)
                        period *= 30;
                    if (rule.getTypePeriod() == 2)
                        period *= 365;
                    if (period >= maxAll && daysInterval == -1) {
                        discount = rule.getPercentage();
                        maxAll = period;
                    } else if (daysInterval >= period && period >= max) {
                        discount = rule.getPercentage();
                        max = period;
                    }
                }
                return discount;
            } else {//default
                double avaragePriceSupplier = 0;
                double priceSell = 0;
                double partialDiscount = 0;
                //calcolo prezzo vendita e media prezzo acquisto
                try {
                    priceSell = daoAutomation.priceSell(connection, productId);
                    avaragePriceSupplier = daoAutomation.searchAverageSupplier(connection, productId);
                } catch (DAOException e) {
                    return 0;
                }
                //fine
                if (daysInterval == -1 || daysInterval > 60) {//mai venduto vendo a prezzo di acquisto
                    partialDiscount = (priceSell - avaragePriceSupplier) * 1;
                } else if (daysInterval >= 30) {//se giorni > 30 prezzo vendita - prezzo acquisto 60%
                    partialDiscount = (priceSell - avaragePriceSupplier) * 0.6;
                } else if (daysInterval >= 20) {//se giorni > 20 prezzo vendita - prezzo acquisto 40%
                    partialDiscount = (priceSell - avaragePriceSupplier) * 0.4;
                } else if (daysInterval >= 10) {//se giorni > 10 prezzo vendita - prezzo acquisto 20%
                    partialDiscount = (priceSell - avaragePriceSupplier) * 0.2;
                } else {
                    return 0;
                }
                return (100 - ((100 * (priceSell - partialDiscount)) / priceSell));
            }
        } else {//Non attivo
            return 0;
        }
    }

    public void OrderManagement(long productId) {
        double[] medieQuantitySell = new double[6];
        double finalAverageQuantitySell = 0;
        boolean averageSellGrowing = false;
        double averagePriceSupplier = 0;
        double minimumPriceSupplier = 0;
        int quantityStock = 0;
        double priceSell = 0;
        int intervalLastOrder = 0;
        int lastQuantityOrder = 0;
        long minQuantityForOrder = automationList.get(1).getStocksRulesList().get(0).getNumber();
        long maxQuantityForOrder = automationList.get(1).getStocksRulesList().get(1).getNumber();
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            //punto f: calcolo quantita presente in magazzino
            quantityStock = DAOAutomation.getInstance().quantityStock(connection, productId);

            if (quantityStock <= minQuantityForOrder) {

                //punto e: costo minimo tra fornitori
                minimumPriceSupplier = DAOAutomation.getInstance().getMinimumPrice(connection, productId);
                long idSupplier = DAOAutomation.getInstance().getMinimumPriceSupplier(connection, productId);
                //punto g: calcolo prezzo di vendita al cliente del prodotto
                priceSell = DAOAutomation.getInstance().priceSell(connection, productId);
                if (priceSell <= minimumPriceSupplier)
                    return;

                double averageFirstMonth = 0, averageSecondMonth = 0;
                //punto a: quantia' media venduta ogni 10 giorni gli ultimi 2 mesi (ottengo 6 medie)
                for (int i = 0; i < medieQuantitySell.length; i++) {
                    medieQuantitySell[i] = DAOAutomation.getInstance().searchQuantityByInterval(connection, (10 * i) + 10, (10 * i), productId);
                    finalAverageQuantitySell += medieQuantitySell[i];
                    if (i < 3)
                        averageFirstMonth += medieQuantitySell[i];
                    else
                        averageSecondMonth += medieQuantitySell[i];
                }

                //punto b: media relativa alle 6 medie ottenute sopra
                finalAverageQuantitySell = finalAverageQuantitySell / 6;
                Double qty = averageFirstMonth * 1.2;
                //punto c: media crescente o descrescente
                averageFirstMonth = averageFirstMonth / 3;
                averageSecondMonth = averageSecondMonth / 3;
                if (averageSecondMonth > averageFirstMonth) {
                    averageSellGrowing = true;
                }else{
                    qty -= qty * 0.4;
                }

                //punto d: costo medio prezzo fornitore relativo agli ordini degli ultimi 2 mesi
                double d = DAOAutomation.getInstance().searchAverageSupplier(connection, productId);
                if (d != 0)
                    averagePriceSupplier = d;

                //punto h: ultima quantità venduta
                lastQuantityOrder = DAOAutomation.getInstance().getLastQuantityOrder(connection, productId);

                //punto i: ultimo ordine di quel prodotto
                intervalLastOrder = DAOAutomation.getInstance().getIntervalOrder(connection, productId);

                System.out.println("Medie: ");
                System.out.println(medieQuantitySell[0] + " " + medieQuantitySell[1] + " " + medieQuantitySell[2] + " " + medieQuantitySell[3] + " " + medieQuantitySell[4] + " " + medieQuantitySell[5]);
                System.out.println("-----------");
                System.out.println("media primo mese: " + averageFirstMonth);
                System.out.println("media secondo mese: " + averageSecondMonth);
                System.out.println("Media crescente: " + averageSellGrowing);
                System.out.println("media prezzo fornitore: " + averagePriceSupplier);
                System.out.println("Media final: " + finalAverageQuantitySell);
                System.out.println("minimo prezzo fornitore: " + minimumPriceSupplier);
                System.out.println("quantita' in magazzino: " + quantityStock);
                System.out.println("prezzo di vendita: " + priceSell);
                System.out.println("ultima quantita' ordinata: " + lastQuantityOrder);
                System.out.println("Intervallo giorni da ultimo ordine: " + intervalLastOrder);
                System.out.println("QTY: " + qty);
                if(qty>1)
                    DAOOrder.getInstance().insert(connection, "Ordine Automatico", idSupplier, productId, qty.intValue());
            }
        } catch (DAOException e) {
            return;
        }
    }
}
