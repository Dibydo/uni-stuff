#include <stdio.h>
#include <stdlib.h>

typedef struct x x;
typedef struct y y;

struct x
{
	int hh1;
	int hh2;
	int total;
};

struct y
{
	int counter;
	int top;
	struct x* heap;
};

void swap (int f, int s, y* inp)
{
	int hh1, hh2, total_sum;
	hh1 = inp -> heap[f].hh1;
	hh2 = inp -> heap[f].hh2;
	total_sum = inp -> heap[f].total;
	inp -> heap[f].hh1 = inp -> heap[s].hh1;
	inp -> heap[f].hh2 = inp -> heap[s].hh2;
	inp -> heap[f].total = inp -> heap[s].total;
	inp -> heap[s].hh1 = hh1;
	inp -> heap[s].hh2 = hh2;
	inp -> heap[s].total = total_sum;
}

void heapify (int count, int num, y* pp)
{
	int r1, r2, r3;
	while (1)
	{
		r1 = 2 * count + 1;
		r2 = r1 + 1;
		r3 = count;
		if ((r1 < num) && (pp -> heap[count].total > pp -> heap[r1].total))
			count = r1;
		if ((r2 < num) && (pp -> heap[count].total > pp -> heap[r2].total))
			count = r2;
		if (count == r3)
			break;
		swap(count, r3, pp);
	}
}

void ins (y* pp, x d)
{
	int h;
	h = pp -> counter;
	pp -> counter++;
	pp -> heap[h] = d;
	while ((h > 0) && (pp -> heap[(h - 1)/2].total > pp -> heap[h].total))
	{
		swap((h - 1)/2, h, pp);
		h = (h-1)/2;
	}
}

x min_op(y* pp)
{
	x mg = pp -> heap[0];
	pp -> counter -= 1;
	if (pp -> counter > 0)
	{
		pp -> heap[0] = pp -> heap[pp->counter];
		heapify(0, pp->counter, pp);
	}
	return mg;
}

int main()
{
	int rr1, rr2, i, imp;
	scanf("%d", &rr2);
	scanf("%d", &rr1);
	y hy;
	hy.top = rr1;
	hy.counter = 0;
	hy.heap = (x*)malloc((rr1 + 1) * sizeof(x));
	x* hc = (x*)malloc((rr1 + 1) * sizeof(x));
	x hh_;
	for (i = 0; i < rr1; ++i)
	{
		scanf("%d", &hc[i].hh1);
		scanf("%d", &hc[i].hh2);
	}
	for (i = 0; i < rr2; ++i)
	{
		hc[i].total = hc[i].hh1 + hc[i].hh2;
		ins(&hy, hc[i]);
	}
	i = rr2;
	while (hy.counter)
	{
		hh_ = min_op(&hy);
		if (i < rr1)
		{
			if (hc[i].hh1 > hh_.total)
				imp = hc[i].hh1;
			else imp = hh_.total;
			hc[i].total = imp + hc[i].hh2;
			ins(&hy, hc[i]);
			i++;
		}
	}
	printf("%d", hh_.total);
	free(hc);
	free(hy.heap);
	return 0;
}