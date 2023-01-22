package com.example.galleryme.data.repository

import com.example.galleryme.core.PageQuery
import com.example.galleryme.core.helper.Resource
import com.example.galleryme.data.datasource.api.service.ImageApiService
import com.example.galleryme.data.datasource.local.dao.ImageDaoService
import com.example.galleryme.data.datasource.local.entity.ImageDBEntity
import com.example.galleryme.data.datasource.local.entity.toImage
import com.example.galleryme.domain.repository.ImageRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ImageRepositoryImplTest {

    @Mock
    private lateinit var imageApiService: ImageApiService

    @Mock
    private lateinit var imageDaoService: ImageDaoService

    private lateinit var imageRepository: ImageRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        this.imageRepository =
            ImageRepositoryImpl(this.imageApiService, this.imageDaoService, Dispatchers.IO)
    }


    /**Should return loading when the request is not completed*/
    @Test
    fun getImagesWhenRequestIsNotCompletedThenReturnLoading() = runTest {
        val pageQuery = PageQuery(1, 20)
        val expected = Resource.loading(null)
        `when`(imageApiService.getImages(pageQuery.page, pageQuery.limit)).thenReturn(emptyList())

        val actual = imageRepository.getImages(pageQuery).first()

        assertEquals(expected, actual)
    }

    /**Should return error when the request is failed*/
    @Test
    fun getImagesWhenRequestIsFailedThenReturnError() = runTest {
        val pageQuery = PageQuery()
        val errorMessage = "Error"
        val error = Exception(errorMessage)
        val expected = Resource.error(errorMessage, null)
        `when`(imageApiService.getImages(pageQuery.page, pageQuery.limit)).doAnswer {
            throw error
        }

        val actual = imageRepository.getImages(pageQuery).last()

        assertEquals(expected, actual)
    }

    /**Should return empty list when the author is not found*/
    @Test
    fun getImagesByAuthorWhenAuthorIsNotFound() = runTest {
        val author = "author"
        val images = listOf<ImageDBEntity>()
        whenever(imageDaoService.loadAllByAuthor(author)).thenReturn(images)

        val result = imageRepository.getImagesByAuthor(author).first()

        assertEquals(result.size, 0)
    }

    /**Should return images when the author is found*/
    @Test
    fun getImagesByAuthorWhenAuthorIsFound() = runTest {
        val author = "author"
        val imageDBEntity = ImageDBEntity("id", author, "url", 1, 1, "downloadUrl")
        val image = imageDBEntity.toImage()
        whenever(imageDaoService.loadAllByAuthor(author)).thenReturn(listOf(imageDBEntity))

        val images = imageRepository.getImagesByAuthor(author).first()

        assertEquals(images.size, 1)
        assertEquals(images[0], image)
    }

    /**Should return authors when the authors is found*/
    @Test
    fun getAuthorsWhenAuthorsIsFound() = runTest {
        val author = "author"
        val expected = listOf(author)
        whenever(imageDaoService.loadAllAuthors()).thenReturn(listOf(author))

        val authors = imageRepository.getAuthors().first()

        assertEquals(expected, authors)
    }
}