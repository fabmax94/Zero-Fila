package com.sidia.fabio.zerofila;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.RemoteViews;

import com.sidia.fabio.zerofila.model.Client;
import com.sidia.fabio.zerofila.persistence.AppDatabase;
import com.sidia.fabio.zerofila.repository.impl.ItemQueueRepository;
import com.sidia.fabio.zerofila.util.OnWidgetHandle;

/**
 * Implementation of App Widget functionality.
 */
public class QueueWidget extends AppWidgetProvider implements OnWidgetHandle {

    private RemoteViews views;
    private ItemQueueRepository itemQueueRepository;
    private AppWidgetManager appWidgetManager;
    private int appWidgetId;
    private Context context;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        views = new RemoteViews(context.getPackageName(), R.layout.queue_widget);
        itemQueueRepository = new ItemQueueRepository();
        GetUserTask getUserTask = new GetUserTask();
        getUserTask.execute(context);
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;
        this.context = context;
    }

    void checkQueue(final Client client) {
        itemQueueRepository.getQueueLength(client.cpf, this);
    }

    private void setTextViews(int length, boolean isAttendance) {
        if (length == -1) {
            views.setTextViewText(R.id.queue_lenght, context.getString(R.string.isnt_queue));
        } else if (length == 0) {
            if (isAttendance) {
                views.setTextViewText(R.id.queue_lenght, context.getString(R.string.is_attendance));
            } else {
                views.setTextViewText(R.id.queue_lenght, context.getString(R.string.is_next));
            }
        } else {
            if (length == 1) {
                views.setTextViewText(R.id.queue_lenght, context.getString(R.string.a_person));
            } else {
                views.setTextViewText(R.id.queue_lenght, length + " " + context.getString(R.string.people));
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void setValue(Pair<Integer, Boolean> result) {
        setTextViews(result.first, result.second);
    }

    class GetUserTask extends AsyncTask<Context, Void, Client> {

        @Override
        protected Client doInBackground(Context... params) {
            AppDatabase app = Room.databaseBuilder(params[0],
                    AppDatabase.class, AppDatabase.DATABASE_ZERO).build();
            return app.clientDAO().getClientSync();
        }

        @Override
        protected void onPostExecute(Client client) {
            if (client != null) {
                checkQueue(client);
            }
        }
    }
}

