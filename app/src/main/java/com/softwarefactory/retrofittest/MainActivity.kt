package com.softwarefactory.retrofittest

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout



class MainActivity : AppCompatActivity() {
    private lateinit var progressBar:ProgressBar
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var postList:MutableList<Post>
    private lateinit var pullToRefresh:SwipeRefreshLayout
    private lateinit var fadeOutAnimation:AlphaAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refreshData()
        pullToRefresh = findViewById(R.id.pulltoRefresh)
        pullToRefresh.setOnRefreshListener {
            refreshData()
            pullToRefresh.isRefreshing = false
        }
        fadeOutAnimation = AlphaAnimation(1.0f,0.0f)
        fadeOutAnimation.duration = 500
        fadeOutAnimation.fillAfter = true
    }
    fun refreshData(){
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        val config = ImageLoaderConfiguration.Builder(this).build()
        ImageLoader.getInstance().init(config)
        postList = ArrayList()


        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofitClient.create(ApiService::class.java)
        val retrofitCall = retrofitService.getPosts()
        progressBar.visibility = View.VISIBLE
        retrofitCall.enqueue(object :Callback<List<Post>>{
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                progressBar.visibility = View.GONE
                progressBar.animation = fadeOutAnimation
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                val list = response.body()
                var posts:Post? = null
                for (i in list!!.indices){
                    posts = Post()
                    val name = list[i].title
                    val color = list[i].id
                    val imageUrl = list[i].photoUrl
                    val price = list[i].photoId
                    posts.photoId = price
                    posts.id = color
                    posts.title = name
                    posts.photoUrl = imageUrl
                    postList.add(posts)
                }
                val recyclerViewAdapter = RecyclerViewAdapter(postList, ImageLoader.getInstance())
                val layoutmanager = GridLayoutManager(this@MainActivity,2)
                recyclerView.addItemDecoration(GridSpacingdecoration(2,10,true))
                recyclerView.layoutManager = layoutmanager
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = recyclerViewAdapter
                progressBar.animation = fadeOutAnimation

            }

        })
    }

    inner class GridSpacingdecoration(private val span: Int, private val space: Int, private val include: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % span

            if (include) {
                outRect.left = space - column * space / span
                outRect.right = (column + 1) * space / span

                if (position < span) {
                    outRect.top = space
                }
                outRect.bottom = space
            } else {
                outRect.left = column * space / span
                outRect.right = space - (column + 1) * space / span
                if (position >= span) {
                    outRect.top = space
                }
            }
        }
    }
}
