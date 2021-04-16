package com.example.canyouguesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ImageView startImageView;
    TextView startTextView;
    ImageView playerImageView;
    Bitmap myPlayerImage;
    Bitmap playerImage;
    Button button0,button1,button2,button3;
    TextView pointsTextView;
    TextView timerTextView;
    int correctAnswerPosition;
    int score=0;
    int playerIndex=0;
    int numberOfQuestion=0;
    Boolean end=false;
    TextView remarksTextView;
    Button playAgain;
    ArrayList<String> playerNames=new ArrayList<String>();
    ArrayList<String> playerUrl=new ArrayList<String>();
    ArrayList<String> answer=new ArrayList<String>();
    ArrayList<String> alreadyOccured=new ArrayList<String>();
    public  void playAgain(View view){
        score=0;
        numberOfQuestion=0;
        end=false;
        timerTextView.setText("30s");
        pointsTextView.setText("00/00");
        playAgain.setVisibility(View.INVISIBLE);
        remarksTextView.setText("");
        generatePlayer();
        alreadyOccured.clear();

        playerImageView.setVisibility(View.VISIBLE);
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);

        new CountDownTimer(30100,500){

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.valueOf(millisUntilFinished/1000)+"s");

            }

            @Override
            public void onFinish() {
                end=true;
                playAgain.setVisibility(View.VISIBLE);
                remarksTextView.setText("Your Score: "+Integer.toString(score) + "/" + Integer.toString(numberOfQuestion));
                remarksTextView.setVisibility(View.VISIBLE);


                playerImageView.setVisibility(View.INVISIBLE);
                button0.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.INVISIBLE);


            }
        }.start();

    }

    public void generatePlayer() {
        if (end == false) {
            answer.clear();
            Random rand = new Random();

            playerIndex = rand.nextInt(playerNames.size() - 1);
            while (alreadyOccured.contains(playerNames.get(playerIndex))) {
                playerIndex = rand.nextInt(playerNames.size() - 1);
            }
            correctAnswerPosition = rand.nextInt(4);
            int incorrectPlayerIndex;
            String url=playerUrl.get(playerIndex);


           DownloadImage task = new DownloadImage();
            try {
                playerImage = task.execute("https://firebasestorage.googleapis.com/v0/b/smart-lock-84ad4.appspot.com/o/User1%2Fuploads?alt=media&token=3c36953d-ad5f-4f6e-987c-70311fbd6025").get();
                playerImageView.setBackground(null);
                playerImageView.setImageBitmap(playerImage);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


                for (int i = 0; i < 4; i++) {
                    if (i == correctAnswerPosition) {
                        answer.add(playerNames.get(playerIndex));
                    } else {
                        incorrectPlayerIndex = rand.nextInt(playerNames.size());
                        while (incorrectPlayerIndex == playerIndex) {
                            incorrectPlayerIndex = rand.nextInt(playerNames.size());
                        }
                        answer.add(playerNames.get(incorrectPlayerIndex));

                    }
                }
                if (playerImageView != null) {
                    button0.setText(answer.get(0));
                    button1.setText(answer.get(1));
                    button2.setText(answer.get(2));
                    button3.setText(answer.get(3));

                }
                alreadyOccured.add(playerNames.get(playerIndex));
            }


        }

    public void chooseAnswer(View view) {
        if (end == false) {
            Log.i("tag :", (String) view.getTag());
            int UserAnswer = Integer.parseInt((String) view.getTag());
            if (UserAnswer == correctAnswerPosition) {
                Toast.makeText(getApplicationContext(), "Correct! ", Toast.LENGTH_LONG).show();
                score++;
                numberOfQuestion++;
            } else {
                Toast.makeText(getApplicationContext(), "Wrong!, He is " + playerNames.get(playerIndex), Toast.LENGTH_LONG).show();
                numberOfQuestion++;
            }
            pointsTextView.setText(Integer.toString(score) + "/" + Integer.toString(numberOfQuestion));

            generatePlayer();


        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0=(Button)findViewById(R.id.button0);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        playerImageView=(ImageView)findViewById(R.id.playerImageView);
        timerTextView=(TextView)findViewById((R.id.timerTextView));
        pointsTextView=(TextView)findViewById(R.id.pointsTextView);
        playAgain=(Button)findViewById(R.id.playAgainButton);
        remarksTextView=(TextView)findViewById(R.id.remarkTextView);
        String names[] = {"Paul Pogba","Lionel Messi", "Virgil van Dijk", "Sadio Mané", "Cristiano Ronaldo", "Mohamed Salah", "Kylian Mbappé", "Robert Lewandowski",
                "Raheem Sterling", "Alisson", "Frenkie de Jong", "Roberto Firmino", "Kevin De Bruyne", "Sergio Agüero", "Eden Hazard", "Harry Kane",
                "Bernardo Silva", "Trent Alexander-Arnold", "Matthijs de Ligt", "Son Heung-min", "Karim Benzema", "Marc-André ter Stegen", "N'Golo Kanté",
                "Andrew Robertson", "Antoine Griezmann", "Luis Suárez", "Dusan Tadic", "Pierre-Emerick Aubameyang", "Hakim Ziyech",
                "Kalidou Koulibaly", "Neymar Jr", "Sergio Ramos","Romelu Lukaku","Luka Modric","Paulo Dybala","Fabinho","Ángel Di María","Marcelo"};
        String urls[] = {"https://talksport.com/wp-content/uploads/sites/5/2018/08/Pogba.jpg?strip=all&w=960&quality=100",
                "https://www.biography.com/.image/c_fit%2Ccs_srgb%2Cfl_progressive%2Cq_auto:good%2Cw_620/MTQ4MDU5NDU0MzgwNzEzNDk0/lionel_messi_photo_josep_lago_afp_getty_images_664928892_resizedjpg.jpg",
                "https://cdn.thisisfutbol.com/wp-content/uploads/2018/12/Virgil-van-Dijk-goal.jpg",
                "https://images.daznservices.com/di/library/GOAL/a2/ec/sadio-mane-liverpool-2019-20_14uvaynrbh2121g3q7o2kla4vc.jpg?t=1826142671&quality=100",
                "https://i0.wp.com/metro.co.uk/wp-content/uploads/2020/06/GettyImages-1163338011.jpg?quality=90&strip=all&zoom=1&resize=644%2C429&ssl=1",
                "https://ss.thgim.com/third-party/opta/article31611767.ece/alternates/FREE_380/salah-cropped1l2p7m2thjdki1rqavqsxjxd8rjpg",
                "https://en.as.com/futbol/imagenes/2020/04/18/primera/1587216590_707890_1587216802_noticia_normal.jpg",
                "https://img.fcbayern.com/image/fetch/f_auto,h_880,q_auto:good,w_660/https://fcbayern.com/binaries/content/gallery/fc-bayern/homepage/saison-19-20/profis/lewandowski/191001_lewandowski_ima.jpg/191001_lewandowski_ima.jpg/fcbhippo%3Alargeninetotwelve%3Fv%3D1579095524765",
                "https://www.thesun.co.uk/wp-content/uploads/2019/12/NINTCHDBPICT000542802019-e1575667449815.jpg",
                "https://metro.co.uk/wp-content/uploads/2020/03/PRI_141814476.jpg?quality=90&strip=all",
                "https://sportstar.thehindu.com/football/article30424690.ece/ALTERNATES/LANDSCAPE_1200/frenkiedejong-cropped1az5t1wbyucrb1fog3no2x5vpfjpg",
                "https://amp.independent.ie/incoming/ec5ef/38923939.ece/AUTOCROP/w300/tyImages_1197911720.jpg",
                "https://resources.premierleague.com/photos/2019/10/30/48dfd36b-6e00-44f5-964e-a52224442b02/KDBassists.jpg?width=930&height=620",
                "https://www.telegraph.co.uk/content/dam/football/2019/12/26/TELEMMGLPICT000189910573_trans%2B%2Bvf93cyop1gpr4w56dB_PHD3uocoo2XZSur4LFGMQIMo.jpeg",
                "https://www.essentiallysports.com/wp-content/uploads/Eden-Hazard.jpg",
                "https://en.as.com/en/imagenes/2020/04/12/football/1586678237_139234_noticia_normal.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTfJNEwisyr-fK5Yz8rz5uBjj6YaQIyEegPu5hs9d1IoFQrsoTPBqGWPEoD&usqp=CAU&ec=45673586",
                "https://talksport.com/wp-content/uploads/sites/5/2020/03/NINTCHDBPICT000570198969-e1584982046610.jpg?strip=all&w=960&quality=100",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRyWCbl4hc5ECdJ4awYKjdEWQ-cUhCQOYJ5kmftJFajW7QopwQdcFUUoZ2O&usqp=CAU&ec=45673586",
                "https://resources.premierleague.com/premierleague/photo/2018/12/11/e68f846f-6273-44a9-a818-a90785a5c97d/Son.jpg",
                "https://images2.minutemediacdn.com/image/fetch/w_736,h_485,c_fill,g_auto,f_auto/https%3A%2F%2Ffansided.com%2Fwp-content%2Fuploads%2Fgetty-images%2F2019%2F12%2F1184808387-850x560.jpeg",
                "https://www.fcbarcelonanoticias.com/uploads/s1/27/55/80/ter-stegen-celebration-275580.jpeg",
                "https://cdn.images.express.co.uk/img/dynamic/67/590x/N-Golo-Kante-989260.jpg?r=1532501775985",
                "https://cdn.vox-cdn.com/thumbor/SlPHKu9sp0aUI7yRYl5BCcpzIC8=/1400x1400/filters:format(jpeg)/cdn.vox-cdn.com/uploads/chorus_asset/file/19570857/1191338345.jpg.jpg",
                "https://i.insider.com/5d8cdcfc2e22af5cd26c0f7f?width=1100&format=jpeg&auto=webp",
                "https://www.fcbarcelona.com/photo-resources/2019/07/10/3e54c127-d844-4ee1-a377-4f47a4e7972f/su-rez2.jpg?width=1200&height=750",
                "https://gmsrp.cachefly.net/images/19/04/23/013e4f8cfd27210debc9edab17ee4fe8/320.jpg",
                "https://tmssl.akamaized.net/images/foto/normal/pierre-emerick-aubameyang-arsenal-1517735439-13859.jpg",
                "https://images.beinsports.com/8trZzpeXjowyUL92x4vCALWWneQ=/full-fit-in/1000x0/hakim-ziyech_7e7f70nja7im1ibuih6utgiwi.jpg",
                "https://cdn.images.express.co.uk/img/dynamic/67/590x/Liverpool-1276813.jpg?r=1588389897834",
                "https://i.pinimg.com/originals/28/bf/3d/28bf3d791d2c8684f8e1e0b9cd32c4a1.jpg",
                "https://www.thesun.co.uk/wp-content/uploads/2019/05/NINTCHDBPICT000460790773-e1559035732807.jpg",
                "https://i0.wp.com/www.numberofthings.com/wp-content/uploads/2019/05/Romelu-Lukaku.jpg?fit=768%2C432&ssl=1",
                "https://sportstar.thehindu.com/third-party/opta/article29959164.ece/ALTERNATES/LANDSCAPE_1200/lukamodric-croppedfhe30oi30hjl1pj9alikn4kqnjpg",
                "https://specials-images.forbesimg.com/imageserve/1137698188/960x0.jpg?cropX1=599&cropX2=4666&cropY1=105&cropY2=2903",
                "https://d3j2s6hdd6a7rg.cloudfront.net/v2/uploads/media/default/0002/04/thumb_103316_default_news_size_5.jpeg",
                "https://images.daznservices.com/di/library/omnisport/5f/15/angel-di-maria-cropped_6adkghz809nb1jhtw0vl5n8h1.jpg?t=-890043906&quality=100",
                "https://www.dayafterindia.com/wp-content/uploads/2019/03/marcelo.jpg",
        };

        int n = names.length;
        int u = urls.length;
        for (int i = 0; i < n; i++) {
            playerNames.add(names[i]);
            playerUrl.add(urls[i]);
        }
        generatePlayer();
        playAgain(findViewById(R.id.playerImageView));
        alreadyOccured.clear();



    }

    public class DownloadImage extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url=new URL(urls[0]);
                HttpsURLConnection httpsURLConnection=(HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();
                InputStream in=httpsURLConnection.getInputStream();
                myPlayerImage= BitmapFactory.decodeStream(in);
                return  myPlayerImage;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}