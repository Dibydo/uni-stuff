#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int return_b(int f, int s)
{
	if (f > s)
		return f;
	else return s;
}

int return_sm(int f, int s)
{
	if (f < s)
		return f;
	else return s;
}

int is_peak(int* arr, int f, int s)
{
	if ((f == 0 && s == 0) || (f == 0 && arr[f] > arr[f + 1]) || (f == s && arr[f - 1] <= arr[f]) || ((arr[f - 1] <= arr[f]) && (arr[f] >= arr[f + 1])))
		return 1;
	if ((f == 0 && arr[f] <= arr[f + 1]) || (f == s && arr[f - 1] > arr[f]))
		return 0;
	else return 0;
}

int new_peak(int *arr, int f, int s, int t)
{
	if ((f == 0 && t == 0) || (f == 0 && s > arr[f + 1]) || (f == t && arr[f - 1] <= s) || ((arr[f - 1] <= s) && (s >= arr[f + 1])))
		return 1;
	if ((f == 0 && s <= arr[f + 1]) || (f == t && arr[f - 1] > s))
		return 0;
	else return 0;
}

int new_peak_left(int *arr, int f, int s, int t)
{
	if ((f == 0 && t == 0) || (f == 0 && array[f] > s) || (f == t && array[f - 1] <= array[f]) || ((array[f - 1] <= array[f]) && (array[f] >= s)))
		return 1;
	if ((f == 0 && array[f] <= s) || (f == t && array[f - 1] > array[f]))
		return 0;
	else return 0;
}

int new_peak_right(int *arr, int f, int s, int t)
{
	if ((f == 0 && t == 0) || (f == 0 && arr[f] > arr[f + 1]) || (f == t && s <= arr[f]) || ((s <= arr[f]) && (arr[f] >= arr[f + 1])))
		return 1;
	if ((f == 0 && arr[f] <= arr[f + 1]) || (f == t && s > arr[f]))
		return 0;
	else return 0;
}

void builder(int* arr, int f, int s, int* inp, int hh, int inp_)
{
	int mid;
	if (f == s)
		inp[hh] = is_peak(arr, f, inp_ - 1);
	else
	{
		mid = (f + s) / 2;
		builder(arr, f, mid, inp, hh * 2, inp_);
		builder(arr, mid + 1, s, inp, hh * 2 + 1, inp_);
		inp[hh] = inp[hh * 2] + inp[hh * 2 + 1];
	}
}

void updater(int* arr1, int* arr2, int l, int r, int ot, int nv, int hh1, int hh2)
{
	int mid;
	mid = (l + r) / 2;
	if (l == r)
	{
		arr2[hh1 - 1] = new_peak(arr1, ot, nv, hh2 - 1);
		arr2[hh1 - 2] = new_peak_left(arr1, ot - 1, nv, hh2 - 1);
		arr2[hh1] = new_peak_right(arr1, ot + 1, nv, hh2 - 1);
	}
	else
	{
		if (ot < mid)
			updater(arr1, arr2, l, mid, ot, nv, hh1 * 2, hh2);
		else updater(arr1, arr2, mid + 1, r, ot, nv, hh1 * 2 + 1, hh2);
	arr2[hh1 - 1] = arr2 [hh * 2] + arr2[hh * 2 + 1];
	arr2[1] = arr2[0];
	}
}

int maximus(int* arr, int f, int s, int hh1, int l, int r)
{
	int mid;
	mid = (f + s) / 2;
	if (l == f && r == s)
		return arr[hh1];
	else
	{
		if (r <= mid)
			maximus(arr, f, mid, hh1 * 2, l, r);
		else
		{
			if (l > mid)
				maximus(arr, mid + 1, s, hh1 * 2 + 1, l, r);
			else return maximus(arr, f, mid, hh1 * 2, l, return_sm(r, mid)) + (maximus(arr, mid + 1, s, hh1 * 2 + 1, return_b(l, mid + 1), r));
		}
	}
}

int main()
{
	int hh1, hh2, hh3, hh4, hh5, hh6;
	hh3 = 0;
	char act[5];
	scanf("%d", &hh1);
	int* arr1 = (int*)malloc(hh1 * sizeof(int));
	for (int i = 0; i < hh1; i++)
		scanf("%d", &arr1[i]);
	int* arr2 = (int*)malloc(5 * hh1 * sizeof(int));
	for (int i = 0; i < 4 * hh1; i++)
		arr2[i] = 0;
	builder(arr1, 0, hh1 - 1, arr2, 1, hh1);
	int* arr3 = (int*)malloc(20001);
	scanf("%d", &hh2);
	for (int i = 0; i < hh2; i++)
	{
		scanf("%s %d %d", act, &hh5, &hh6);
		if (strcmp(act, "PEAK") == 0)
		{
			arr3[hh3] = maximus(arr2, 0, hh1 - 1, 1, hh5, hh6);
			hh3 += 1;
		}
		else updater(arr1, arr2, 0, hh1 - 1, hh5, hh6, 1, hh1);
	}
	for (int i = 0; i < hh3; i++)
		printf("%d\n", arr3[i]);
	free(arr1);
	free(arr2);
	free(arr3);
	return 0;
}