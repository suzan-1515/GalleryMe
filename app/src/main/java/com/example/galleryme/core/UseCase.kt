package com.example.galleryme.core

interface UseCase<R> {
    suspend fun execute():R
}

interface UseCaseWithParams<R, P> {
    suspend fun execute(params: P):R
}