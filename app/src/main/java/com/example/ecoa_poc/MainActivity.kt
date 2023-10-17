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
        var previousSVGPath:SVGPath? = null


        val androidRichPathView = findViewById<SVGPathView>(R.id.view)
        var previousColor:Int =0
        androidRichPathView.onPathClickListener = object : SVGPath.OnPathClickListener {
            override fun onClick(svgPath: SVGPath) {
                if (svgPath.name !=null){
                    if (previousSVGPath!=null){
                        val storedObj =previousSVGPath!!.name?.let {
                            androidRichPathView.findRichPathByName(it)
                        }
                        storedObj?.fillColor = previousColor
                        previousSVGPath = svgPath
                        previousColor = svgPath.fillColor
                        svgPath.fillColor = Color.BLUE
                    }else{
                        previousColor = svgPath.fillColor
                        svgPath.fillColor= Color.BLUE
                        previousSVGPath = svgPath
                    }

                    getMapList().get(svgPath.name)?.let {
                        setAlert(it.toString())
                    }
                }
            }
        }
    }

    fun getMapList():HashMap<String, String>{
        val theMap = HashMap<String, String>()
        theMap["backHead"] = "The part of the maxilla in which the 4 upper incisors develop, which forms the primary palate, and underlies the philtrum and upper lip."
        theMap["leftHeap"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftSolder"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftBack"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backLeftThigh"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backLeftLeg"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backRightLeg"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftBackHand"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftHand"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftLittleFinger"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftRingFinger"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftMidleFinger"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftIndexFinger"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["leftThumb"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backRightHip"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backRightBack"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["backRightHand"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"


        theMap["frontHead"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftChest"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["rightChest"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftSolder"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftBishapes"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftHand"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftHand"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftKnee"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftLeg"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftLeg"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftLeg"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["frontLeftPalm"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["rightSolder"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["rightPlam"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"
        theMap["face"] = "According to astrology, a man's right itchy palm indicates good days. Right wrist or palm itching predicts the possibility of getting unexpected financial assistance. The person might get delayed payment. They could profit from a lot of things, win the lottery, or by making getting gains in the stock market"

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