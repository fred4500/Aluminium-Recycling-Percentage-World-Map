/**
 * Copyright 2019 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.mycompany.app;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private MapView mapView;

    public static void main(String[] args) {

        Application.launch(args);
    }
    public static void addNewPoint(int x, int y, Color color, int radius, GraphicsOverlay graphicsOverlay, SpatialReference wgs84) {
        Point point = new Point(x, y, wgs84);
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, radius);
        graphicsOverlay.getGraphics().add(new Graphic(point, simpleMarkerSymbol));
    }

    public static void addNewLine(Point start, Point end, Color color, GraphicsOverlay graphicsOverlay, SpatialReference wgs84) {
        PointCollection points = new PointCollection(wgs84);
        points.add(start);
        points.add(end);
        Polyline line = new Polyline(points);
        SimpleLineSymbol lineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, color, 3);
        graphicsOverlay.getGraphics().add(new Graphic(line, lineSymbol));
    }

    @Override
    public void start(Stage stage) {

        // set the title and size of the stage and show it
        stage.setTitle("My Map App");
        stage.setWidth(800);
        stage.setHeight(700);
        stage.show();

        // create a JavaFX scene with a stack pane as the root node and add it to the scene
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);

        // Note: it is not best practice to store API keys in source code.
        // An API key is required to enable access to services, web maps, and web scenes hosted in ArcGIS Online.
        // If you haven't already, go to your developer dashboard to get your API key.
        // Please refer to https://developers.arcgis.com/java/get-started/ for more information
        String yourApiKey = "AAPTxy8BH1VEsoebNVZXo8HurMRTbUOiwuaU_N-WVDnghMcbkfxEG6ZSD5OJE0-kbB7VaOltiLlC3MDK7brAu0jN0RCd_j-VpVaZ7qOAgvvyqoIh2BxPx4wQEJV5ElKHJ5XzdLOPJ-OFm5la5u9SjRlokcHJFg7dGmtqZpycxBTtrvUcr4csHrNg-Rg2GGztcGqg3yOV1s_EezV0U21plAQK9AuXFWnSxzSjXP_kjhKMxms.AT1_YKBFzOom";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        // create a MapView to display the map and add it to the stack pane
        mapView = new MapView();
        stackPane.getChildren().add(mapView);

        // create an ArcGISMap with an imagery basemap
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_LIGHT_GRAY);
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        SpatialReference wgs84 = SpatialReference.create(4326);

        addNewPoint(10,10, Color.AQUA, 10, graphicsOverlay, wgs84);

        addNewLine(new Point(0,0,wgs84), new Point(3,3, wgs84), Color.ORANGE, graphicsOverlay, wgs84);
        // display the map by setting the map on the map view
        mapView.setMap(map);
    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {

        if (mapView != null) {
            mapView.dispose();
        }
    }
}
