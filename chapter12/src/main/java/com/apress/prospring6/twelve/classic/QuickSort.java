/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring6.twelve.classic;

/**
 * Created by iuliana on 24/10/2022
 */
public final class QuickSort  extends AbstractSort {

    public QuickSort(int[] arr) {
        super(arr);
        name = "QuickSort";
    }

    @Override
    public void sort(int[] arr) {
        sort(arr, 0, arr.length - 1);
    }


    /**
     * {@link "https://youtu.be/ywWBy6J5gz8" target="_blank"> How it works}
     *
     * @param arr int array to be sorted
     * @param low lower limit of the interval to be sorted
     * @param high higher limit of the interval to be sorted
     */
    private void sort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);

            sort(arr, low, pivot - 1);
            sort(arr, pivot + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];

        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
