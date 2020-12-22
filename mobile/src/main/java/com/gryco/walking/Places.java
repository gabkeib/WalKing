package com.gryco.walking;

import com.google.maps.model.LatLng;

/**
 * Created by Gabrielius on 2019.01.27.
 */

public class Places {
    public String snippets[] = {
      "Viewpoint of St. Matas church - the highest church in Lithuania. The height of this church reaches 79 metres. You can reach this viewpoint by climbing a lot of metal steps untill you reach the height of 33 metres. That is your goal! When you are finally there you will find yourself standing under all three bells of the church and the view of Anykščiai will be a pleasure for your eyes",
            "„Laimės žiburys“ - a tomb of Jonas Biliūnas, a big writer and a great cultural figure of Lithuania. You will find this memorial on the top of the mound of Liudiškės. The natural beauty and fairness of the forest from the top of this mound is breathtaking. And as you are climbing the steps to the top, don't forget to count each and every one of them! When you are finally there, make a wish and watch it come true!",
            "The museum of horses - the only museum of this kind in Baltic states. It is located in a village Niūronys, near Anykščiai. The museum holds a variety of tools representing a long history of lithuanians keeping horses and doing farm work using their help. The whole teritory includes 16 biuldings with exhibitions, a centre of crafts and houses built with tradicional patterns of this region. The museum is constantly being updated, there are educational programs where you can watch and learn local crafts. No one could imagine a museum of horses without live exhibits so here you can pet and ride horses or eaven a carriage. You can also find writer's Jonas Biliūnas home - mueum here and take a look at beautiful work of local sculptors.",
            "Puntukas - the second largest rock in Lithuania. Don't be frightened by its' weight but it's about 265 tones! Rock's lenght is 7,54 m , width - 7,34 m and height - 5,7 m. Archeologists claim, that Puntukas glided to Anykščiai together with the glaciers of Scandinavian mountains long time ago. But if you expected something more mysteriuos, a local legend says that one day the devil himself was carrying this rock with the intention to shatter the church but when the sun suddenly came up and roosters started to sing the devil spooked out and dropped it right were he was. Originally the rock was named after a hero Puntukas.",
            "Treetop walking path - the first pathway in the Baltic region where you can actually walk on top of trees. From this pathway you can get the most amazing view of relatively small but famous in all country forest Anykščių šilelis, curves of the river Šventoji and if you look closely, you can spot two towers of the tallest church in Lithuania - St. Matas church. The pathway continues for 300 metres and as you go, you will gradually get to 21 metres above the ground. But that's not the end. The tree top pathway ends up with even higher 34 metres tower. Incredible!"
    };

    public com.google.android.gms.maps.model.LatLng locations[] = {
        new com.google.android.gms.maps.model.LatLng(55.524478, 25.100715),
            new com.google.android.gms.maps.model.LatLng(55.511912, 25.122980),
            new com.google.android.gms.maps.model.LatLng(55.572743, 25.086115),
            new com.google.android.gms.maps.model.LatLng(55.484196, 25.059661),
            new com.google.android.gms.maps.model.LatLng(55.485471, 25.060495)
    };

    public String names[] = {
            "Viewpoint of St. Matas church",
            "Laimės žibūrys",
            "The museum of horses",
            "Puntukas",
            "Treetop walking path"
    };

    public int images[] = {
            R.drawable.baznycia,
            R.drawable.laimes,
            R.drawable.horse,
            R.drawable.puntukas,
            R.drawable.treetop
    };

    public String getSnippet(int a){
        String s = snippets[a];
        return s;
    }

    public String getName(int a){
        String s = names[a];
        return s;
    }
    public com.google.android.gms.maps.model.LatLng getLatlng(int a){
        com.google.android.gms.maps.model.LatLng s = locations[a];
        return s;
    }
    public int getImage(int a){
        int s = images[a];
        return s;
    }
}
