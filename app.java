import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class app{

    private static JFrame frame;
    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static String fetchButton;
    private static String apiKey="9c776d04d97ab5e91c5cd7d7f5cac800";//replace your own generated key


    private static String fetchWeatherData(String city){
        try {
            URI uri = new URI("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response="";
            String line;
            while((line=reader.readLine())!=null){
                response+=line;
            }
            reader.close();

            JSONObject jsonObject = (JSONObject) JSONValue.parse(response.toString());
            JSONObject mainObj = (JSONObject) jsonObject.get("main");

            double temperatureKelvin = (double)mainObj.get("temp");
            long humidity  =(long)mainObj.get("humidity");

            //convert to celcius
            double temperatureCelcius = temperatureKelvin-273.15;

            //retrieve weather description
            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String description=(String) weather.get("description");

            return "Description: " + description + "\ntemperature: "
            + temperatureCelcius + " Celsius\nHumidity: "+ humidity + "%";
        } catch (Exception e) {
            return "Failed to fetch weather data. Please check your city and api key";
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Forecast App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLayout(new FlowLayout());

        JTextField locationField= new JTextField(15);
        JButton fetchButton = new JButton("Fetch Weather");
        JTextArea weatherDisplay = new JTextArea(10,30);
        weatherDisplay.setEditable(false);

        frame.add(new JLabel("Enter City Name"));
        frame.add(locationField);
        frame.add(fetchButton);
        frame.add(weatherDisplay);

        fetchButton.addActionListener((ActionListener) new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e){
                String city = locationField.getText();
                String weatherInfo = fetchWeatherData(city);
                weatherDisplay.setText(weatherInfo);
            }
        });

        frame.setVisible(true);


    }
}