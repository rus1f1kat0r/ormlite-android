package com.j256.ormlite.android.apptools.support;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * A {@link android.content.Loader} implementation that queries specified {@link com.j256.ormlite.dao.Dao} using a {@link com.j256.ormlite.stmt.PreparedQuery}.
 *
 * @author Egorand
 */
public class OrmLitePreparedQueryLoader<T, ID> extends BaseOrmLiteLoader<T, ID> {

	private final PreparedQuery<T> preparedQuery;

	public OrmLitePreparedQueryLoader(Context context, Dao<T, ID> dao, PreparedQuery<T> preparedQuery) {
		super(context, dao);
		this.preparedQuery = preparedQuery;
	}

	@Override
	protected List<T> runQuery(Dao<T, ID> dao) throws SQLException {
		return dao.query(preparedQuery);
	}
}
