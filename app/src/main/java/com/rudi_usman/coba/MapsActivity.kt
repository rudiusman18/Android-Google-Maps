package com.rudi_usman.coba

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.rudi_usman.coba.databinding.ActivityMapsBinding
import com.rudi_usman.coba.models.penerimaBnpt
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var addFAB: FloatingActionButton
    private lateinit var personFAB:FloatingActionButton
    private lateinit var electricityFAB:FloatingActionButton
    private lateinit var  flagFAB:FloatingActionButton
    private lateinit var  starFAB:FloatingActionButton
    var state:String = ""
    var fabVisible = false
    private lateinit var mapFragment:SupportMapFragment
    val malang = LatLng(-7.9666204, 112.63263210000002)
    val indonesia = LatLng(-6.200000, 106.816666)
    var options:Boolean = false





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // FloatinG Action Button here
        addFAB = findViewById(R.id.FABadd)
        personFAB = findViewById(R.id.FABperson)
        electricityFAB = findViewById(R.id.FABelectricity)
        flagFAB = findViewById(R.id.FABflag)
        starFAB = findViewById(R.id.FABall)


        // addFAB onclick listener
        addFAB.setOnClickListener{
            if(!fabVisible){
                personFAB.show()
                electricityFAB.show()
                flagFAB.show()
                starFAB.show()

                personFAB.visibility = View.VISIBLE
                electricityFAB.visibility = View.VISIBLE
                flagFAB.visibility = View.VISIBLE
                starFAB.visibility = View.VISIBLE

                addFAB.setImageResource(R.drawable.ic_round_remove_circle_24)
                Log.i("1", "FAB Opened")
                fabVisible = true
            }else{
                personFAB.hide()
                electricityFAB.hide()
                flagFAB.hide()
                starFAB.hide()

                personFAB.visibility = View.GONE
                electricityFAB.visibility = View.GONE
                flagFAB.visibility = View.GONE
                starFAB.visibility = View.GONE
                addFAB.setImageResource(R.drawable.ic_baseline_add_circle_24)
                Log.i("2", "FAB Closed")
                fabVisible = false
            }

        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            var success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.google_maps_style));

            if (!success) {
                Log.e("Maps Activity", "Style parsing failed.");
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Maps Activity", "Can't find style. Error: ", e);
        }

        map = googleMap
        flagFAB.setOnClickListener {
            options = true
           if (state !=  "flag"){
               map.clear()
               mapFragment.getMapAsync(this)
               val layer1 = GeoJsonLayer(map, R.raw.risiko_bencana_gabungan, this)
               layer1.defaultPolygonStyle.fillColor = R.color.teal_700
               layer1.defaultPolygonStyle.strokeWidth = 5f
               layer1.removeLayerFromMap()
               layer1.addLayerToMap()
               state = "flag"

           }
        }

        electricityFAB.setOnClickListener {

            options = true
            if (state != "electricity"){
                map.clear()
                mapFragment.getMapAsync(this)
                val layer = GeoJsonLayer(map, R.raw.jaringan_listrik_kota_malang, this)

                layer.defaultLineStringStyle.color = Color.BLUE
                layer.defaultLineStringStyle.width = 5f
                layer.removeLayerFromMap()
                layer.addLayerToMap()
                state = "electricity"

            }


        }

        personFAB.setOnClickListener {
            options = true

            if (state != "person") {
                map.clear()
                mapFragment.getMapAsync(this)
                val jsonFileString =
                    getJsonDataFromAsset(applicationContext, "1_total_penerima_bpnt_blimbing.json")
                val gson = Gson()
//        Mengambil data bnpt untuk marker
                val penerimaBnptType = object : TypeToken<penerimaBnpt>() {}.type
                val bnpt: penerimaBnpt = gson.fromJson(jsonFileString, penerimaBnptType)
                bnpt.multi_location.forEachIndexed { id, item ->
                    var marker = LatLng(item.latitude!!.toDouble(), item.longitude!!.toDouble())
                    map.addMarker(MarkerOptions().position(marker).title(item.title)
                        .snippet(item.mini_info!!.split("<br>")[0] + "\nAlamat: " + item.address))
                }
                map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
                state = "person"
            }
        }

        starFAB.setOnClickListener {
            options = true


            if (state != "all") {
                map.clear()
                mapFragment.getMapAsync(this)
//            Polygon
                val layer1 = GeoJsonLayer(map, R.raw.risiko_bencana_gabungan, this)
                layer1.defaultPolygonStyle.fillColor = R.color.teal_700
                layer1.defaultPolygonStyle.strokeWidth = 5f
                layer1.removeLayerFromMap()
                layer1.addLayerToMap()

//            Polyline
                val layer = GeoJsonLayer(map, R.raw.jaringan_listrik_kota_malang, this)
                layer.defaultLineStringStyle.color = Color.BLUE
                layer.defaultLineStringStyle.width = 5f
                layer.removeLayerFromMap()
                layer.addLayerToMap()

//            Marker
                val jsonFileString =
                    getJsonDataFromAsset(applicationContext, "1_total_penerima_bpnt_blimbing.json")
                val gson = Gson()
//        Mengambil data bnpt untuk marker
                val penerimaBnptType = object : TypeToken<penerimaBnpt>() {}.type
                val bnpt: penerimaBnpt = gson.fromJson(jsonFileString, penerimaBnptType)
                bnpt.multi_location.forEachIndexed { id, item ->
                    var marker = LatLng(item.latitude!!.toDouble(), item.longitude!!.toDouble())
                    map.addMarker(MarkerOptions().position(marker).title(item.title)
                        .snippet(item.mini_info!!.split("<br>")[0]))
                }
                map.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
                state = "all"
            }

        }

        if (options == false){
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(indonesia))
        }else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malang, 15f))
        }

       }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
//        Mengubah tipe maps berdasarkan pilihan pengguna
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else->super.onOptionsItemSelected(item)
    }


    }


    private fun setMapLongClick(map: GoogleMap){
        map.setOnMapClickListener {
            latLng -> map.addMarker(MarkerOptions().position(latLng))
//            Snippet adalah teks tambahan yang ditampilkan setelah judul
            val snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, %2$.5f", latLng.latitude, latLng.longitude)
            map.addMarker(MarkerOptions().position(latLng).title("Dropped Pin").snippet(snippet))
        }
    }

    private fun setPOIClick(map:GoogleMap){
        map.setOnPoiClickListener{
            poi -> val poiMarker = map.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
            poiMarker?.showInfoWindow()
        }
    }





