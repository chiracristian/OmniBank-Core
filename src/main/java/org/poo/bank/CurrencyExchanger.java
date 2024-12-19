package org.poo.bank;

import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.poo.fileio.ExchangeInput;

import java.util.List;

@Getter
public final class CurrencyExchanger {
    private final Graph<String, Double> conversionRates;
    private final BFSShortestPath<String, Double> conversionPaths;

    /**
     * Construct a new currency exchanger
     * @param exchangeInputs the known exchange rates
     */
    public CurrencyExchanger(final ExchangeInput[] exchangeInputs) {
        conversionRates = new DefaultDirectedWeightedGraph<>(Double.class);

        for (ExchangeInput currentIn : exchangeInputs) {
            String from = currentIn.getFrom();
            String to = currentIn.getTo();

            conversionRates.addVertex(from);
            conversionRates.addVertex(to);

            double directRate = currentIn.getRate();
            double inverseRate = 1.0 / directRate;

            conversionRates.addEdge(from, to, directRate);
            conversionRates.addEdge(to, from, inverseRate);
        }

        conversionPaths = new BFSShortestPath<>(conversionRates);
    }

    /**
     * Convert an amount from a currency to another
     * @param amountToExchange the initial amount to convert
     * @param from the initial currency
     * @param to the desired currency
     * @return the amount converted in the desired currency
     */
    public double convert(final double amountToExchange, final String from, final String to) {
        List<Double> requiredConversions = conversionPaths.getPath(from, to).getEdgeList();

        double result = amountToExchange;

        for (Double currentConversion : requiredConversions) {
            result *= currentConversion;
        }

        return result;
    }
}
