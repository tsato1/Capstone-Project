package com.takahidesato.android.promatchandroid.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by tsato on 5/23/16.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetDataProvider dataProvider = new WidgetDataProvider(getApplicationContext(), intent);
        return dataProvider;
    }
}
