package com.j256.ormlite.android.apptools.loader.support;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

/**
 * Created by kgalligan on 3/23/15.
 */
public abstract class AbstractOrmLiteLoader<T, DO> extends AsyncTaskLoader<DO> implements Dao.DaoObserver
{
	protected final Dao<T, ?> dao;
	protected final PreparedQuery<T> query;
	protected DO databaseResults;

	public AbstractOrmLiteLoader(Context context, Dao<T, ?> dao, PreparedQuery<T> query) {
		super(context);
		this.dao = dao;
		this.query = query;
	}

	@Override
	public void deliverResult(DO newCursor) {
		if (isReset()) {
			// an async query came in while the loader is stopped
			resetData(newCursor);
			return;
		}

		DO oldCursor = databaseResults;
		databaseResults = newCursor;

		if (isStarted()) {
			super.deliverResult(newCursor);
		}

		// close the old cursor if necessary
		if (oldCursor != null && oldCursor != newCursor) {
			resetData(oldCursor);
		}
	}

	@Override
	protected void onStartLoading() {
		// start watching for dataset changes
		dao.registerObserver(this);

		if (databaseResults == null) {
			forceLoad();
		} else {
			deliverResult(databaseResults);
			if (takeContentChanged()) {
				forceLoad();
			}
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(DO cursor) {
		resetData(cursor);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		resetData(databaseResults);
		databaseResults = null;

		// stop watching for changes
		dao.unregisterObserver(this);
	}

	protected abstract void resetData(DO databaseResults);

	public void onChange() {
		onContentChanged();
	}
}