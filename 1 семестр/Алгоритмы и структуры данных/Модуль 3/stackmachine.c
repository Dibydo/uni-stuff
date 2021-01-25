#include <stdio.h>
#include <stdlib.h>

int main()
{
	int hh1, co;
	long hh2;
	char expr[5];
	hh2 = 0;
	co = 0;
	long* arr = (long*)malloc(100000 * sizeof(long));
	scanf("%d", &hh1);
	for (int i = 0; i < hh1; i++)
	{
		scanf("%s", &expr);
		if (strcmp(expr, "CONST") == 0)
		{
			scanf("%ld", &hh2);
			*(arr + co) = hh2;
			co++;
		}
		if (strcmp(expr, "ADD") == 0)
		{
			co--;
			*(arr + co - 1) = *(arr + co) + *(arr + co - 1);	
		}
		if (strcmp(expr, "SUB") == 0)
		{
			co--;
			*(arr + co - 1) = *(arr + co) - *(arr + co - 1);
		}
		if (strcmp(expr, "MUL") == 0)
		{
			co--;
			*(arr + co - 1) = *(arr + co) * (*(arr + co -1));
		}
		if (strcmp(expr, "DIV") == 0)
		{
			co--;
			*(arr + co - 1) = *(arr + co)/(*(arr + co - 1));
		}
		if (strcmp(expr, "MAX") == 0)
		{
			co--;
			if (*(arr + co) > (*(arr + co - 1)))
				*(arr + co - 1) = *(arr + co);
			else *(arr + co - 1) = *(arr + co - 1);
		}
		if (strcmp(expr, "MIN") == 0)
		{
			co--;
			if (*(arr + co) > (*(arr + co - 1)))
				*(arr + co - 1) = *(arr + co - 1);
			else *(arr + co - 1) = *(arr + co);
		}
		if (strcmp(expr,"NEG") == 0) 
		{
			co--;
			hh2 = *(arr + co);
			hh2 = hh2 - 2 * hh2;
			*(arr + co) = hh2;
			co++;
		}
		if (strcmp(expr,"DUP") == 0)
		{
			*(arr + co)=*(arr + co - 1);
			co++;
		}
		if (strcmp(expr,"SWAP") == 0)
		{
		    co--;
            hh2 = *(arr + co - 1);
            *(arr + co - 1) = *(arr + co);
            *(arr + co) = hh2;
            co++;
        }
	}
	printf("%ld", *(arr + co - 1));
	free(arr);
	return 0;
}