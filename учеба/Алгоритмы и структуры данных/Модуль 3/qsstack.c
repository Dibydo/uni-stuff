#include <stdio.h>
#include <stdlib.h>

typedef struct Task ta;
typedef struct Stack ins;

struct Task
{
	int low, high;
};

struct Stack
{
	struct Task* inf;
	int nc;
	int highst;
};

void swapper(int* inp1, int* inp2)
{
	int temp;
	temp = *inp1;
	*inp1 = *inp2;
	*inp2 = temp;
}

void stack_op(ins* inp1, int inp2)
{
	inp1 -> nc = inp2;
	inp1 -> highst = 0;
	inp1 -> inf = malloc((sizeof(struct Task)) * (inp2 + 1));
}

void push(ins* st, int lb, int rb)
{
	st -> inf[st -> highst].low = lb;
	st -> inf[st -> highst].high = rb;
	st -> highst++;
}

int t_st_emp(ins* st)
{
	if (st -> highst == 0)
		return 1;
	else return 0;
}

ta pop(ins* st)
{
	ta p;
	st -> highst--;
	p = st -> inf[st -> highst];
	return p;
}

void sorting(int* arr, int inp)
{
	int l, r, mid, hh;
	ins st;
	stack_op(&st, 5000);
	push(&st, 0, inp - 1);
	while (!(t_st_emp(&st)))
	{
		ta harr = pop(&st);
		while (harr.low < harr.high)
		{
			mid = (harr.low + harr.high) / 2;
			l = harr.low;
			r = harr.high;
			hh = arr[mid];
			do
			{
				while (arr[l] < hh)
					l++;
				while (arr[r] > hh)
					r--;
				if (l <= r)
				{
					swapper(&arr[l], &arr[r]);
					l++;
					r--;
				}
			} while (l <= r);
			if (l < mid)
			{
				if (l < harr.high)
				{
					push(&st, l, harr.high);
				}
				harr.high = r;
			}
			else
			{
				if (r > harr.low)
					push(&st, harr.low, r);
				harr.low = l;
			}
		}
	}
	free(st.inf);
}

int main()
{
	int size;
	scanf("%d", &size);
	int* ha = malloc((size + 1) * sizeof(int));
	for (int i = 0; i < size; i++)
		scanf("%d", &ha[i]);
	sorting(ha, size);
	for (int i = 0; i < size; i++)
		printf("%d ", ha[i]);
	printf("\n");
	free(ha);
	return 0;
}