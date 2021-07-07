package barrios.abrahan.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import barrios.abrahan.myfeelings.utilities.CustomBarDrawable
import barrios.abrahan.myfeelings.utilities.CustomCircleDrawable
import barrios.abrahan.myfeelings.utilities.Emociones
import barrios.abrahan.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy= 0.0F
    var happy= 0.0F
    var neutral= 0.0F
    var sad= 0.0F
    var verySad= 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    var graphVeryHappy: View? = null
    var graphHappy: View? = null
    var graphNeutral: View? = null
    var graphSad: View? = null
    var graphVerySad: View? = null
    var graph: View? = null
    var btnGuardar: Button? = null
    var btnVH: ImageButton? = null
    var btnH: ImageButton? = null
    var btnN: ImageButton? = null
    var btnS: ImageButton? = null
    var btnVS: ImageButton? = null

    var icon:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphVeryHappy= findViewById<View>(R.id.graphVeryHappy)
        graphHappy= findViewById<View>(R.id.graphHappy)
        graphNeutral= findViewById<View>(R.id.graphNeutral)
        graphSad= findViewById<View>(R.id.graphTriste)
        graphVerySad= findViewById<View>(R.id.graphVerySad)
        graph= findViewById<View>(R.id.graph)
        btnGuardar= findViewById<Button>(R.id.guardarButton)
        btnVH= findViewById<ImageButton>(R.id.veryHappyButton)
        btnH= findViewById<ImageButton>(R.id.happyButton)
        btnN= findViewById<ImageButton>(R.id.neutralButton)
        btnS= findViewById<ImageButton>(R.id.sadButton)
        btnVS= findViewById<ImageButton>(R.id.verySadButton)
        icon= findViewById<ImageView>(R.id.icon)

        jsonFile = JSONFile()

        fetchingData()
        if(!data){
            var emociones= ArrayList<Emociones>()
            val fondo= CustomCircleDrawable(this,emociones)
            graph?.background = fondo
            graphVeryHappy?.background= CustomBarDrawable(this,Emociones("Muy feliz",0.0F,R.color.deepBlue,veryHappy))
            graphHappy?.background= CustomBarDrawable(this,Emociones("Feliz",0.0F,R.color.blue,happy))
            graphNeutral?.background= CustomBarDrawable(this,Emociones("Neutral",0.0F,R.color.greenie,neutral))
            graphSad?.background= CustomBarDrawable(this,Emociones("Triste",0.0F,R.color.orange,sad))
            graphVerySad?.background= CustomBarDrawable(this,Emociones("Muy triste",0.0F,R.color.mustard,verySad))
        } else{
            actualizarGrafica()
            iconoMayoria()
        }

        btnGuardar?.setOnClickListener {
            guardar()
        }

        btnVH?.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        btnH?.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }
        btnN?.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }
        btnS?.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        btnVS?.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }

    }

    fun fetchingData(){

        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json!=""){
                this.data=true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista){

                    when(i.nombre){
                        "Muy feliz" -> veryHappy= i.total
                        "Feliz" -> happy= i.total
                        "Neutral" -> neutral= i.total
                        "Triste" -> sad= i.total
                        "Muy triste" -> verySad= i.total
                    }
                }
            } else {
                this.data=false
            }
        } catch (exception: JSONException){
            exception.printStackTrace()
        }

    }

    fun iconoMayoria(){

        if(happy>veryHappy && happy>neutral&&happy>sad&&happy>verySad){
            icon?.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(veryHappy>happy && veryHappy>neutral&&veryHappy>sad&&veryHappy>verySad){
            icon?.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }

        if(neutral>happy && neutral>veryHappy &&neutral>sad&&neutral>verySad){
            icon?.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }

        if(sad>happy && sad>veryHappy &&sad>neutral&&sad>verySad){
            icon?.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }

        if(verySad>happy && verySad>happy &&verySad>neutral&&verySad>sad){
            icon?.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }

    fun actualizarGrafica(){

        val total= veryHappy+happy+neutral+verySad+sad

        var pVH: Float = (veryHappy*100 / total).toFloat()
        var pH: Float = (happy* 100 / total).toFloat()
        var pN: Float = (neutral* 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verySad * 100 / total).toFloat()
        Log.d("porcentajes", "Very happy" +pVH)
        Log.d( "porcentajes","happy"+pH)
        Log.d( "porcentajes","neutral"+pN)
        Log.d( "porcentajes","sad "+pS)
        Log.d( "porcentajes","very sad"+pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones ("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue,sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue,verySad))

        val fondo = CustomCircleDrawable(this, lista)


        graphVeryHappy?.background= CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy?.background= CustomBarDrawable( this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral?.background = CustomBarDrawable(this, Emociones( "Neutral",pN, R.color. greenie, neutral))
        graphSad?.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad?.background = CustomBarDrawable( this, Emociones(  "Muy triste", pVS, R.color.deepBlue, verySad))
        graph?.background=fondo


    }

    fun parseJson (jsonArray: JSONArray): ArrayList<Emociones>{

        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()){

            try {
                val nombre= jsonArray.getJSONObject(i). getString("nombre")
                val porcentaje = jsonArray.getJSONObject (i). getDouble( "porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i). getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()

                var emocion = Emociones (nombre, porcentaje, color, total )
                lista.add(emocion)
            } catch (exception : JSONException){
                exception.printStackTrace()
            }
        }
        return lista

    }

    fun guardar(){

        var jsonArray= JSONArray()
        var o: Int = 0
        for (i in lista){
            Log.d("objetos",i.toString())
            var j:JSONObject= JSONObject()
            j.put("Nombre", i.nombre)
            j.put("porcentaje",i.porcentaje)
            j.put("color",i.color)
            j.put("total",i.total)

            jsonArray.put(o,j)
            o++
        }

        jsonFile?.saveData(this,jsonArray.toString())

        Toast.makeText(this,"Datos Guardados",Toast.LENGTH_SHORT).show()
    }
}