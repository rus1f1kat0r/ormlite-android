package com.j256.ormlite.android.apptools.loader.support;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kgalligan on 3/23/15.
 */
public class OrmLiteListLoader<T> extends com.j256.ormlite.android.apptools.loader.AbstractOrmLiteLoader<T, List<T>>
{
	public OrmLiteListLoader(Context context, Dao<T, ?> dao, PreparedQuery<T> query)
	{
		super(context, dao, query);
	}

	@Override
	protected void resetData(List<T> databaseResults)
	{
		//Do nothing
	}

	@Override
	public List<T> loadInBackground()
	{
		try
		{
			return dao.query(query);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
