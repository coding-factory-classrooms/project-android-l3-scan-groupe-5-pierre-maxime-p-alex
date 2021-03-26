package com.pierre.yugiohkotlinapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.pierre.yugiohkotlinapp.api.RetrofitInstance
import com.pierre.yugiohkotlinapp.cards.CardService
import com.pierre.yugiohkotlinapp.cards.ProDeckModels
import com.pierre.yugiohkotlinapp.recycler.CardListAdapter
import com.pierre.yugiohkotlinapp.recycler.CardsListActivity
import com.pierre.yugiohkotlinapp.room.CardApplication
import com.pierre.yugiohkotlinapp.room.CardEntity
import com.pierre.yugiohkotlinapp.room.CardViewModel
import com.pierre.yugiohkotlinapp.room.CardViewModelFactory
import com.pierre.yugiohkotlinapp.utils.Utils
import kotlinx.android.synthetic.main.activity_manual.*
import kotlinx.android.synthetic.main.activity_scan.*
import retrofit2.Response
import java.lang.Double.parseDouble
import java.util.*

class ScanActivity : AppCompatActivity() {

	private val MY_PERMISSIONS_REQUEST_CAMERA: Int = 101
	private lateinit var cameraSource: CameraSource
	private lateinit var textRecognizer: TextRecognizer

	private val cardViewModel: CardViewModel by viewModels {
		CardViewModelFactory((application as CardApplication).repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_scan)
		val adapter = CardListAdapter()

		cardViewModel.allCards.observe(this) { card ->
			// Update the cached copy of the cards in the adapter.
			card.let { adapter.submitList(it) }
		}

		//  Create text Recognizer
		textRecognizer = TextRecognizer.Builder(this).build()
		if (!textRecognizer.isOperational) {
			Toast.makeText(
				this,
				"Dependencies are not loaded yet...please try after few moment!!",
				Toast.LENGTH_SHORT
			).show()
			return
		}

		//  Init camera source to use high resolution and auto focus
		cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
			.setFacing(CameraSource.CAMERA_FACING_BACK)
			.setRequestedPreviewSize(1280, 1024)
			.setAutoFocusEnabled(true)
			.setRequestedFps(3.0f)
			.build()


		surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {

			override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
			}

			override fun surfaceDestroyed(p0: SurfaceHolder) {
				cameraSource.stop()
			}

			override fun surfaceCreated(p0: SurfaceHolder) {
				try {
					if (isCameraPermissionGranted()) {
						if (ActivityCompat.checkSelfPermission(
								this@ScanActivity,
								Manifest.permission.CAMERA
							) != PackageManager.PERMISSION_GRANTED
						) {
							requestForPermission()
							return
						}
						cameraSource.start(surface_camera_preview.holder)
					} else {
						requestForPermission()
					}
				} catch (e: Exception) {
					Toast.makeText(this@ScanActivity, "Error :" + e.message, Toast.LENGTH_SHORT)
						.show()
				}
			}

		})

		textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
			override fun release() {}

			override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
				val items = detections.detectedItems

				if (items.size() <= 0) {
					return
				}

				tv_result.post {
					val stringBuilder = StringBuilder()
					for (i in 0 until items.size()) {
						val item = items.valueAt(i)
						stringBuilder.append(item.value)
						stringBuilder.append("\n")
					}
					tv_result.text = stringBuilder.toString()
					var numeric = true

					try {
						val num = parseDouble(stringBuilder.toString())
					} catch (e: NumberFormatException) {
						numeric = false
					}

					if (stringBuilder.length == 9 && numeric) {
						Handler(Looper.getMainLooper()).postDelayed({
							searchCardById(this@ScanActivity, stringBuilder.toString())
						}, 1000)
					}
				}
			}
		})
	}

	fun searchCardById(view: ScanActivity, stringBuilder: String) {

		val retrofitService = RetrofitInstance
			.getRetrofitInstance()
			.create(CardService::class.java)

		val responseLiveData: LiveData<Response<ProDeckModels>> = liveData {
			val response = retrofitService.getCardByString(stringBuilder)
			emit(response)
		}

		responseLiveData.observe(this, Observer {
			val cardsData = it.body()
			if (cardsData?.cardsData == null) {
				Utils.showAlert(
					"Carte introuvable",
					applicationContext,
					this
				)
				return@Observer
			}

			val cardBody = cardsData.cardsData.first()

			val cardData = CardEntity(
				0,
				cardBody.name,
				cardBody.desc,
				cardBody.id,
				cardBody.cardImages.first().imageUrl,
				cardBody.atk,
				cardBody.def,
				cardBody.level,
				cardBody.race,
				"${cardBody.cardPrices.first().ebayPrice} $"
			)

			val cardDatabase = cardViewModel.allCards.value

			if (cardDatabase != null) {
				val isExist = cardDatabase.any { it.cardId == cardBody.id }
				if (isExist) {
					Utils.showAlert(
						"Carte déjà sauvegardée",
						applicationContext,
						this
					)
					return@Observer
				}
				Thread(Runnable {
					cardViewModel.insert(cardData)
				}).start()
			}

			Utils.showAlert(
				"Carte ajoutée",
				applicationContext,
				this
			)
			val intent = Intent(this, CardsListActivity::class.java)
			startActivity(intent)

		})
	}

	private fun requestForPermission() {
		if (ContextCompat.checkSelfPermission(
				this@ScanActivity,
				Manifest.permission.CAMERA
			)
			!= PackageManager.PERMISSION_GRANTED
		) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(
					this@ScanActivity,
					Manifest.permission.CAMERA
				)
			) {

			} else {

				ActivityCompat.requestPermissions(
					this@ScanActivity,
					arrayOf(
						Manifest.permission.CAMERA
					),
					MY_PERMISSIONS_REQUEST_CAMERA
				)
			}
		} else {
			// Permission has already been granted
		}
	}

	private fun isCameraPermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(
			this@ScanActivity,
			Manifest.permission.CAMERA
		) == PackageManager.PERMISSION_GRANTED
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>, grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		when (requestCode) {
			MY_PERMISSIONS_REQUEST_CAMERA -> {
				// If request is cancelled, the result arrays are empty.
				if ((grantResults.isNotEmpty() &&
							grantResults[0] == PackageManager.PERMISSION_GRANTED)
				) {
					// Permission is granted. Continue the action or workflow
					// in your app.
				} else {
					requestForPermission()
				}
				return
			}

			// Add other 'when' lines to check for other
			// permissions this app might request.
			else -> {
				// Ignore all other requests.
			}
		}
	}
}
