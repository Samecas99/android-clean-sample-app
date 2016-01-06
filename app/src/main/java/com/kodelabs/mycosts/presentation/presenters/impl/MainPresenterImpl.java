package com.kodelabs.mycosts.presentation.presenters.impl;

import com.kodelabs.mycosts.domain.executor.Executor;
import com.kodelabs.mycosts.domain.executor.MainThread;
import com.kodelabs.mycosts.domain.interactors.DeleteCostInteractor;
import com.kodelabs.mycosts.domain.interactors.GetCostByIdInteractor;
import com.kodelabs.mycosts.domain.interactors.GetCostsInteractor;
import com.kodelabs.mycosts.domain.interactors.impl.DeleteCostInteractorImpl;
import com.kodelabs.mycosts.domain.interactors.impl.GetCostByIdInteractorImpl;
import com.kodelabs.mycosts.domain.interactors.impl.GetCostsInteractorImpl;
import com.kodelabs.mycosts.domain.model.Cost;
import com.kodelabs.mycosts.presentation.converter.DailyTotalCostConverter;
import com.kodelabs.mycosts.presentation.model.DailyTotalCost;
import com.kodelabs.mycosts.presentation.presenters.AbstractPresenter;
import com.kodelabs.mycosts.presentation.presenters.MainPresenter;
import com.kodelabs.mycosts.storage.CostRepositoryImpl;

import java.util.Date;
import java.util.List;

/**
 * Created by dmilicic on 12/13/15.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter,
        GetCostsInteractor.Callback,
        GetCostByIdInteractor.Callback,
        DeleteCostInteractor.Callback {

    private MainPresenter.View mView;

    public MainPresenterImpl(Executor executor, MainThread mainThread,
                             View view) {
        super(executor, mainThread);
        mView = view;
    }

    @Override
    public void resume() {
        getAllCosts();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void getAllCosts() {
        // get all costs
        GetCostsInteractor getCostsInteractor = new GetCostsInteractorImpl(mExecutor, mMainThread,
                CostRepositoryImpl.getInstance(), this, new Date(), new Date());
        getCostsInteractor.execute();
    }

    @Override
    public void onCostsRetrieved(List<Cost> costList) {
        List<DailyTotalCost> dailyTotalCosts = DailyTotalCostConverter.convertCostsToDailyCosts(costList);
        mView.showCosts(dailyTotalCosts);
    }

    @Override
    public void deleteCost(long costId) {

        // delete this cost item in a background thread
        DeleteCostInteractor deleteCostInteractor = new DeleteCostInteractorImpl(mExecutor,
                mMainThread, costId, this, CostRepositoryImpl.getInstance());
        deleteCostInteractor.execute();
    }

    @Override
    public void getCostById(long id) {
        GetCostByIdInteractor costByIdInteractor = new GetCostByIdInteractorImpl(
                mExecutor, mMainThread, id, CostRepositoryImpl.getInstance(), this);
        costByIdInteractor.execute();
    }

    @Override
    public void onCostRetrieved(Cost cost) {
        mView.onCostRetrieved(cost);
    }

    @Override
    public void onCostDeleted(Cost cost) {
        mView.onCostDeleted(cost);
    }
}
