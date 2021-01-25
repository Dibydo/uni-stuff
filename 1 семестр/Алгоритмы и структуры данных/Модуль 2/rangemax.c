#include <stdio.h>
#include <stdlib.h>

int return_bigger(int a, int b)
{
	if (a > b)
		return a;
	else return b;
}

int return_smaller(int a, int b)
{
	if (a < b)
		return a;
	else return b;
}

int l2rel(int inp)
{
	int i = 0;
	inp = inp >> 1;
	for (i = 0; inp != 0;)
	{
		inp = inp >> 1;
		i += 1;
	}
	return i;
}

void builder(int* inp_arr, int f, int s, int* addit, int hh)
{
	int mid = (f + s) / 2;
	if (f == s)
		addit[hh] = inp_arr[s];
	else
	{
		builder(inp_arr, f, mid, addit, hh * 2);
		builder(inp_arr, mid + 1, s, addit, hh * 2 + 1);
		addit[hh] = return_bigger(addit[hh * 2],addit[hh * 2 + 1]);
	}
}

void updater(int* inp_arr, int f, int s, int co, int add_var, int hh)
{
	int mid = (f + s) / 2;
	if (f == s)
		inp_arr[hh] = add_var;
	else
	{
		if (co <= mid)
			updater(inp_arr, f, mid, co, add_var, hh * 2);
		else updater(inp_arr, mid + 1, s, co, add_var, hh * 2 + 1);
		inp_arr[hh] = return_bigger(inp_arr[hh* 2], inp_arr[hh * 2 + 1]);
	}
}

int f_bt(int* inp_arr, int f, int s, int hh, int lb, int rb)
{
	int mid = (f + s) / 2;
	if (f == lb & s == rb)
		return inp_arr[hh];
	else
	{
		if (rb <= mid)
			f_bt(inp_arr, f, mid, hh * 2, lb, rb);
		else
		{
			if (lb > mid)
				f_bt(inp_arr, mid + 1, s, hh * 2 + 1, lb, rb);
			else return return_bigger((f_bt(inp_arr, f, mid, hh * 2, lb, return_smaller(rb, mid))), (f_bt(inp_arr, mid + 1, rb, hh * 2 + 1, return_bigger(lb, mid + 1), rb)));
		}
	}
}

int main()
{
	int size, hh1, hh2, hh3, lb, rb;
	char act[4];
	hh3 = 0;
	scanf("%d", &size);
	int* arr1 = (int*)malloc(size * sizeof(int));
	int* arr2 = (int*)malloc(4 * size * sizeof(int));
	for (int i = 0; i < size; i++)
		scanf("%d", &arr1[i]);
	builder(arr1, 0, size - 1, arr2, 1);
	scanf("%d", &hh1);
	int* arr3 = (int*)malloc((hh1 + 1) * sizeof(int));
	for (int i = 0; i < hh1; hh1++)
	{
		scanf("%s %d %d", &act, &lb, &rb);
		if (strcmp(act, "MAX") == 0)
		{
			*(arr3 + hh3) = f_bt(arr2, 0, size - 1, 1, lb, rb);
			hh3 += 1;
		}
		else updater(arr2, 0, size - 1, lb, rb, 1);
	}
	for (int i = 0; i < hh3; i++)
		printf("%d\n", arr3[i]);
	free(arr1);
	free(arr2);
	free(arr3);
	return 0;
}