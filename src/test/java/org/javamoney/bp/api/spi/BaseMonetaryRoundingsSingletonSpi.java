/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.CurrencyUnit;
import org.javamoney.bp.api.MonetaryException;
import org.javamoney.bp.api.MonetaryRounding;
import org.javamoney.bp.api.RoundingQuery;
import org.javamoney.bp.api.RoundingQueryBuilder;

import java.util.Collection;

/**
 * This class models the accessor for rounding instances, modeled as
 * {@link org.javamoney.bp.api.MonetaryOperator}.
 * <p>
 * This class is thread-safe.
 *
 * @author Anatole Tresch
 * @author Werner Keil
 */
public abstract class BaseMonetaryRoundingsSingletonSpi implements MonetaryRoundingsSingletonSpi {

    /**
     * Access a {@link org.javamoney.bp.api.MonetaryRounding} for rounding {@link org.javamoney.bp.api.MonetaryAmount}
     * instances given a currency.
     *
     * @param currencyUnit The currency, which determines the required precision. As
     *                     {@link java.math.RoundingMode}, by default, {@link java.math.RoundingMode#HALF_UP}
     *                     is sued.
     * @param providers    the optional provider list and ordering to be used
     * @return a new instance {@link org.javamoney.bp.api.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws org.javamoney.bp.api.MonetaryException if no such rounding could be provided.
     */
    public MonetaryRounding getRounding(CurrencyUnit currencyUnit, String... providers) {
        MonetaryRounding op =
                getRounding(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
        if(op==null) {
            throw new MonetaryException(
                    "No rounding provided for CurrencyUnit: " + currencyUnit.getCurrencyCode());
        }
        return op;
    }


    /**
     * Access a {@link org.javamoney.bp.api.MonetaryRounding} using the rounding name.
     *
     * @param roundingName The rounding name, not null.
     * @param providers    the optional provider list and ordering to be used
     * @return the corresponding {@link org.javamoney.bp.api.MonetaryOperator} implementing the
     * rounding, never {@code null}.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link RoundingProviderSpi} instance.
     */
    public MonetaryRounding getRounding(String roundingName, String... providers) {
        MonetaryRounding op =
                getRounding(RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingName).build());
        if(op==null) {
            throw new MonetaryException("No rounding provided with rounding name: " + roundingName);
        }
        return op;
    }


    /**
     * Query a specific rounding with the given query. If multiple roundings match the query the first one is
     * selected, since the query allows to determine the providers and their ordering by setting {@link
     * org.javamoney.bp.api.RoundingQuery#getProviderNames()}.
     *
     * @param query the rounding query, not null.
     * @return the rounding found, or null, if no rounding matches the query.
     */
    public MonetaryRounding getRounding(RoundingQuery query) {
        Collection<MonetaryRounding> roundings = getRoundings(query);
        if (roundings.isEmpty()) {
            return null;
        }
        return roundings.iterator().next();
    }

    /**
     * Checks if any {@link org.javamoney.bp.api.MonetaryRounding} is matching the given query.
     *
     * @param query the rounding query, not null.
     * @return true, if at least one rounding matches the query.
     */
    public boolean isRoundingAvailable(RoundingQuery query) {
        return !getRoundings(query).isEmpty();
    }

    /**
     * Checks if a {@link org.javamoney.bp.api.MonetaryRounding} is available given a roundingId.
     *
     * @param roundingId The rounding identifier.
     * @param providers  the providers and ordering to be used. By default providers and ordering as defined in
     *                   #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.api.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link org.javamoney.bp.api.spi.RoundingProviderSpi} instance.
     */
    public boolean isRoundingAvailable(String roundingId, String... providers) {
        return isRoundingAvailable(
                RoundingQueryBuilder.of().setProviderNames(providers).setRoundingName(roundingId).build());
    }

    /**
     * Checks if a {@link org.javamoney.bp.api.MonetaryRounding} is available given a {@link org.javamoney.bp.api.CurrencyUnit}.
     *
     * @param currencyUnit The currency, which determines the required precision. As {@link java.math.RoundingMode},
     *                     by default, {@link java.math.RoundingMode#HALF_UP} is used.
     * @param providers    the providers and ordering to be used. By default providers and ordering as defined in
     *                     #getDefaultProviders is used.
     * @return true, if a corresponding {@link org.javamoney.bp.api.MonetaryRounding} is available.
     * @throws IllegalArgumentException if no such rounding is registered using a
     *                                  {@link org.javamoney.bp.api.spi.RoundingProviderSpi} instance.
     */
    public boolean isRoundingAvailable(CurrencyUnit currencyUnit, String... providers) {
        return isRoundingAvailable(RoundingQueryBuilder.of().setProviderNames(providers).setCurrency(currencyUnit).build());
    }
}
