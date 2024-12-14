package org.poo.bank;

import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.poo.fileio.ExchangeInput;

import java.util.List;

@Getter
public class CurrencyManager {
    private final Graph<String, Double> conversionRates;
    private final BFSShortestPath<String, Double> conversionPaths;

    public CurrencyManager(ExchangeInput[] exchangeInputs) {
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

    public double convert(double amountToExchange, String from, String to) {
        List<Double> conversionRates = conversionPaths.getPath(from, to).getEdgeList();

        double result = amountToExchange;
        for (Double currentRate : conversionRates) {
            result *= currentRate;
        }
        return result;
    }
}
