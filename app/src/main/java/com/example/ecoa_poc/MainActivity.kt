package com.example.ecoa_poc

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.richpath.SVGPath
import com.richpath.SVGPathView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val androidRichPathView = findViewById<SVGPathView>(R.id.view)
        androidRichPathView.onPathClickListener = object : SVGPath.OnPathClickListener {
            override fun onClick(svgPath: SVGPath) {
                svgPath.fillColor= Color.BLUE
                getMapList().get(svgPath.name)?.let {
                    setAlert(it.toString())
                }
            }
        }
    }

    fun getMapList():HashMap<String, String>{
        val theMap = HashMap<String, String>()
        theMap["leftEye"] = "The eye is made up of three coats, which enclose the optically clear aqueous humour, lens, and vitreous body. The outermost coat consists of the cornea and the sclera; the middle coat contains the main blood supply to the eye and consists, from the back forward, of the choroid, the ciliary body, and the iris."
        theMap["leftHand"] = "Examining data on about 400,000 people, scientists discovered that the left and right hemispheres of the brain were better connected and more coordinated in regions involving language in left-handed people. These traits suggest that left-handed individuals may have superior verbal skills."
        theMap["rightHand"] = "Examining data on about 400,000 people, scientists discovered that the left and right hemispheres of the brain were better connected and more coordinated in regions involving language in left-handed people. These traits suggest that left-handed individuals may have superior verbal skills."
        theMap["rightLowerHand"] = "Examining data on about 400,000 people, scientists discovered that the left and right hemispheres of the brain were better connected and more coordinated in regions involving language in left-handed people. These traits suggest that left-handed individuals may have superior verbal skills."
        theMap["rightPalms"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"

        return theMap
    }

    fun setAlert(msg:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(msg)
            .setTitle("Information About body part")
            .setNegativeButton("Ok") { dialog, which ->
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}