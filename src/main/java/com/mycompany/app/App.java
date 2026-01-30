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
import com.esri.arcgisruntime.symbology.*;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App extends Application {

    private MapView mapView;

    public static void main(String[] args) {

        Application.launch(args);
    }
    public static void addNewPoint(double x, double y, Color color, int radius, GraphicsOverlay graphicsOverlay, SpatialReference wgs84) {
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

    public static void addNewText(String text, int size, Point point, Color color, GraphicsOverlay graphicsOverlay) {
        TextSymbol textSymbol = new TextSymbol(
                size,
                text,
                color,
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.MIDDLE
        );
        graphicsOverlay.getGraphics().add(new Graphic(point, textSymbol));
    }

    public static ArrayList<String[]> readLatLongFile(String fileName) throws IOException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String[]> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] dataLine = line.split(",");
            data.add(dataLine);
        }
        return data;
    }

    @Override
    public void start(Stage stage) throws IOException {

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

        String latLongFileName = "/Users/frederikschabel/IdeaProjects/Aluminium-Recycling-Percentage-World-Map/LatitudeLongitude.csv";
        ArrayList<String[]> latLong = readLatLongFile(latLongFileName);

        // create a MapView to display the map and add it to the stack pane
        mapView = new MapView();
        stackPane.getChildren().add(mapView);

        // create an ArcGISMap with an imagery basemap
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_LIGHT_GRAY);
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        SpatialReference wgs84 = SpatialReference.create(4326);

        //addNewPoint(10,10, Color.AQUA, 10, graphicsOverlay, wgs84);

        //addNewLine(new Point(0,0,wgs84), new Point(3,3, wgs84), Color.ORANGE, graphicsOverlay, wgs84);

        //addNewText("Hi", 12, new Point(20,20, wgs84), Color.BLACK, graphicsOverlay);

        for (int i = 1; i < latLong.size(); i++) {
            String[] row = latLong.get(i);

            // Skip rows with missing lat/lon (e.g. UM)
            if (row.length < 3) continue;
            if (row[1].trim().isEmpty() || row[2].trim().isEmpty()) continue;

            double lat = Double.parseDouble(row[1].trim());
            double lon = Double.parseDouble(row[2].trim());

            // IMPORTANT: x = longitude, y = latitude
            addNewPoint(lon, lat, Color.AQUA, 4, graphicsOverlay, wgs84);
        }

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
