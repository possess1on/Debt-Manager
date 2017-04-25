package com.chikeandroid.debtmanager20.data.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.chikeandroid.debtmanager20.data.PersonDebt;
import com.chikeandroid.debtmanager20.data.source.DebtsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Chike on 4/24/2017.
 * (@link DebtsRespository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class DebtLoader extends AsyncTaskLoader<PersonDebt> implements DebtsRepository.DebtsRepositoryObserver{


    private final String mDebtId;

    private DebtsRepository mDebtsRepository;

    public DebtLoader(Context context, @NonNull DebtsRepository repository, @NonNull String debtId) {
        super(context);
        checkNotNull(repository);
        mDebtsRepository = repository;
        mDebtId = debtId;
    }

    @Override
    public PersonDebt loadInBackground() {
        return mDebtsRepository.getDebt(mDebtId);
    }

    @Override
    public void deliverResult(PersonDebt data) {
        if(isReset()) {
            return;
        }

        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded data immediately if available.
        if(mDebtsRepository.cachedDebtsAvailable()) {
            deliverResult(mDebtsRepository.getCachedDebt(mDebtId));
        }

        // Begin monitoring the underlying data source
        mDebtsRepository.addContentObserver(this);

        if(takeContentChanged() || !mDebtsRepository.cachedDebtsAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    public void onDebtsChanged() {
        if(isStarted()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mDebtsRepository.removeContentObserver(this);
    }
}