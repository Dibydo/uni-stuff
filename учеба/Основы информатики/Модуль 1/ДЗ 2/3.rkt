(define (string-prefix? str str1)
  (or
   (null? (string->list str))
   (begin
     (if (equal? (string-length str) (string-length str1))
         (begin
           (or
            (null? (cdr (string->list str)))
            (begin
              (and
               (equal? (car (string->list str)) (car (string->list str1)))
               (string-prefix? (list->string (cdr (string->list str))) (list->string  (cdr (string->list str))))
               ))))
         (begin
           (if (< (string-length str) (string-length str1))
               (string-prefix? str (list->string (reverse (cdr (reverse (string->list str1))))))
               (string-prefix? (list->string (reverse (cdr (reverse (string->list str))))) str1)))))                 
   ))