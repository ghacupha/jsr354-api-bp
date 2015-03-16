/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU
 * ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF
 * THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE
 * BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency API ("Specification") Copyright
 * (c) 2012-2013, Credit Suisse All rights reserved.
 */
package org.javamoney.bp.api.spi;

import org.javamoney.bp.api.MonetaryAmount;
import org.javamoney.bp.api.MonetaryAmountFactory;
import org.javamoney.bp.api.MonetaryAmountFactoryQuery;
import java.util.Collection;

/**
 * SPI (core) for the backing implementation of the {@link org.javamoney.bp.api.MonetaryAmounts} singleton, implementing
 * the query functionality for amounts.
 *
 * @author Anatole Tresch
 */
public interface MonetaryAmountsSingletonQuerySpi {

    /**
     * Get the {@link org.javamoney.bp.api.MonetaryAmountFactory} implementation class, that best matches to cover the given
     * {@link org.javamoney.bp.api.MonetaryContext}.
     * <p>
     * The evaluation order should consider the following aspects:
     * <ul>
     * <li>If {@link org.javamoney.bp.api.MonetaryContext#getAmountType()} is explicitly defined, it should be considered.
     * Nevertheless if precision/scale cannot be met, a {@link org.javamoney.bp.api.MonetaryException} should
     * be thrown.
     * <li>The remaining implementation class candidates must cover the required precision.
     * <li>The remaining implementation class candidates must cover the required max scale.
     * <li>If max scale is met, but {@code precision==0} (unlimited precision), the
     * {@link org.javamoney.bp.api.MonetaryAmount} implementation candidate should be chosen with highest possible
     * precision.
     * <li>If still multiple implementation candidates qualify, the ones with
     * {@code Flavor.PERFORMANCE} are preferred.
     * <li>After this point the selection may be arbitrary.
     * </ul>
     *
     * @return the {@link org.javamoney.bp.api.MonetaryAmount} implementation class, that best matches to cover the given
     * {@link org.javamoney.bp.api.MonetaryContext}, never {@code null}.
     * @throws org.javamoney.bp.api.MonetaryException if no {@link org.javamoney.bp.api.MonetaryAmount} implementation class can cover
     *                                       the required
     *                                       {@link org.javamoney.bp.api.MonetaryContext}.
     */
    Collection<MonetaryAmountFactory<? extends MonetaryAmount>> getAmountFactories(MonetaryAmountFactoryQuery query);

    /**
     * Checks if an {@link org.javamoney.bp.api.MonetaryAmountFactory} is matching the given query.
     *
     * @param query the factory query, not null.
     * @return true, if at least one {@link org.javamoney.bp.api.MonetaryAmountFactory} matches the query.
     */
    boolean isAvailable(MonetaryAmountFactoryQuery query);

    /**
     * Executes the query and returns the {@link org.javamoney.bp.api.MonetaryAmount} implementation type found,
     * if there is only one type.
     * If multiple types match the query, the first one is selected.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    Class<? extends MonetaryAmount> getAmountType(MonetaryAmountFactoryQuery query);

    /**
     * Executes the query and returns the {@link org.javamoney.bp.api.MonetaryAmount} implementation typea found.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    Collection<Class<? extends MonetaryAmount>> getAmountTypes(MonetaryAmountFactoryQuery query);

    /**
     * Executes the query and returns the {@link org.javamoney.bp.api.MonetaryAmountFactory} implementation type found,
     * if there is only one type. If multiple types match the query, the first one is selected.
     *
     * @param query the factory query, not null.
     * @return the type found, or null.
     */
    MonetaryAmountFactory getAmountFactory(MonetaryAmountFactoryQuery query);

}
