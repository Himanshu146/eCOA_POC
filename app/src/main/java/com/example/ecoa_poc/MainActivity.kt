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
        var selectedSVGPath : ArrayList<SVGPath>? = arrayListOf()


        val androidRichPathView = findViewById<SVGPathView>(R.id.view)
        var previousColor:Int =0
        androidRichPathView.onPathClickListener = object : SVGPath.OnPathClickListener {
            override fun onClick(svgPath: SVGPath) {
                if (selectedSVGPath!=null && selectedSVGPath.size >1) {
                    val index = selectedSVGPath.indexOfLast {
                        it.name == svgPath?.name
                    }

                    if(index>-1){
                        val storedObj =svgPath!!.name?.let {
                            androidRichPathView.findRichPathByName(it)
                        }
                        storedObj?.fillColor = previousColor
                        previousSVGPath = svgPath
                        previousColor = svgPath.fillColor
                        //svgPath.fillColor = Color.BLUE
                        selectedSVGPath.removeAt(index)
                    }else{
                        previousColor = svgPath.fillColor
                        svgPath.fillColor= Color.BLUE
                        previousSVGPath = svgPath
                        selectedSVGPath?.add(svgPath)
                    }
                }else{
                    selectedSVGPath?.add(svgPath)
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

    fun getMapList():HashMap<String, String>{
        val theMap = HashMap<String, String>()
        theMap["backHead"] = "The back of the head houses the brain, which is a vital organ. The skull, a bony structure, surrounds and protects the brain from external injuries. The occipital bone is the specific bone in the back of the skull that helps protect the brain"
        theMap["leftHeap"] = "The hip joint connects the upper body with the lower body and plays a crucial role in bearing the body's weight. It provides stability for activities like standing, walking, running, and maintaining balance."
        theMap["leftSolder"] = "The shoulders are home to several important muscles, including the deltoids, trapezius, and rotator cuff muscles. These muscles enable the movements of the arms and the stability of the shoulder joint."
        theMap["leftBack"] = "The back muscles, particularly the erector spinae, help maintain an upright posture. They keep the spine erect and support it when you're sitting, standing, or performing various activities."
        theMap["backLeftThigh"] = "The muscles in the buttocks are responsible for various movements of the hip joint. They allow you to extend your thigh backward, rotate your hip outward and inward, and move your hip laterally. These movements are important for activities like walking, running, and dancing."
        theMap["backLeftLeg"] = "Strong leg muscles help maintain stability and balance, allowing you to stand and move without falling over. The gluteal muscles, in particular, play a significant role in stabilizing the pelvis and hips."
        theMap["backRightLeg"] = "Strong leg muscles help maintain stability and balance, allowing you to stand and move without falling over. The gluteal muscles, in particular, play a significant role in stabilizing the pelvis and hips."
        theMap["leftBackHand"] = "The human hand has a complex arrangement of muscles that allow for fine motor control and a wide range of movements. These muscles include intrinsic muscles (located within the hand) and extrinsic muscles (originating from the forearm and extending into the hand). The hand muscles are responsible for functions like grasping, manipulating objects, and performing intricate tasks"
        theMap["leftHand"] = "The human hand has a complex arrangement of muscles that allow for fine motor control and a wide range of movements. These muscles include intrinsic muscles (located within the hand) and extrinsic muscles (originating from the forearm and extending into the hand). The hand muscles are responsible for functions like grasping, manipulating objects, and performing intricate tasks"
        theMap["leftLittleFinger"] = "Fingers are crucial for holding objects of various sizes and shapes. This ability to grip and manipulate objects is essential for many everyday tasks, from picking up a pen to using tools and utensils"
        theMap["leftRingFinger"] = "Fingers are rich in sensory receptors, allowing us to feel textures, temperatures, and pressure. This tactile sense is vital for our interactions with the world and helps us detect potential dangers or pleasures"
        theMap["leftMidleFinger"] = "Fingers are rich in sensory receptors, allowing us to feel textures, temperatures, and pressure. This tactile sense is vital for our interactions with the world and helps us detect potential dangers or pleasures"
        theMap["leftIndexFinger"] = "Fingers enable precise movements, making activities such as typing, writing, drawing, and playing musical instruments possible. These fine motor skills are essential for creative and professional tasks."
        theMap["leftThumb"] = "In sign language, fingers are used to convey information, allowing individuals with hearing impairments to communicate effectively."
        theMap["backRightHip"] = "The hip joint connects the upper body with the lower body and plays a crucial role in bearing the body's weight. It provides stability for activities like standing, walking, running, and maintaining balance."
        theMap["backRightBack"] = "The hip joint connects the upper body with the lower body and plays a crucial role in bearing the body's weight. It provides stability for activities like standing, walking, running, and maintaining balance."
        theMap["backRightHand"] = "The human hand has a complex arrangement of muscles that allow for fine motor control and a wide range of movements. These muscles include intrinsic muscles (located within the hand) and extrinsic muscles (originating from the forearm and extending into the hand). The hand muscles are responsible for functions like grasping, manipulating objects, and performing intricate tasks"


        theMap["frontHead"] = "The skull, which is the bony structure that encases the brain, provides protection for this vital organ. The forehead and face also protect the eyes, nose, and mouth from injury."
        theMap["frontLeftChest"] = "The most critical structure in the left chest is the heart, which is a muscular organ responsible for pumping blood throughout the body. The heart is primarily located in the left side of the chest and is responsible for circulating oxygenated blood to the rest of the body."
        theMap["rightChest"] = "The right lung is one of the two lungs in the chest, responsible for the exchange of oxygen and carbon dioxide during respiration."
        theMap["frontLeftSolder"] = "The shoulders are home to several important muscles, including the deltoids, trapezius, and rotator cuff muscles. These muscles enable the movements of the arms and the stability of the shoulder joint."
        theMap["frontLeftBishapes"] = "This system allows for movement and includes skeletal muscles, smooth muscles (found in organs), and cardiac muscles (found in the heart)."
        theMap["frontLeftHand"] = "The human hand has a complex arrangement of muscles that allow for fine motor control and a wide range of movements. These muscles include intrinsic muscles (located within the hand) and extrinsic muscles (originating from the forearm and extending into the hand). The hand muscles are responsible for functions like grasping, manipulating objects, and performing intricate tasks"
        theMap["frontLeftKnee"] = "The knee joint plays a crucial role in supporting the body's weight and allowing you to stand, walk, run, and perform various weight-bearing activities."
        theMap["frontLeftLeg"] = "The left leg, along with the right leg, allows humans to move from one place to another. It's involved in walking, running, jumping, and other forms of locomotion."
        theMap["frontLeftPalm"] = "The left hand, along with the right hand, is essential for tasks that require fine motor skills and precision, such as writing, drawing, typing, using tools, and handling small objects."
        theMap["rightSolder"] = "The shoulders are home to several important muscles, including the deltoids, trapezius, and rotator cuff muscles. These muscles enable the movements of the arms and the stability of the shoulder joint."
        theMap["rightPlam"] = "The right hand plays a crucial role in fine motor skills, such as writing, drawing, painting, and using tools like scissors or a computer mouse."
        theMap["face"] = "The face is a primary means of identifying and recognizing individuals. Humans use facial features, such as the eyes, nose, mouth, and unique facial characteristics, to distinguish one person from another."

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