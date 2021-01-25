(define (delete pred? xs)
  (cond
    ((null? xs) xs)
    ((pred? (car xs)) (delete pred? (cdr xs)))
    (else (cons (car xs) (delete pred? (cdr xs))))))

(delete even? '(0 1 2 3))
(delete even? '(0 2 4 6))
(delete even? '(1 3 5 7))
(delete even? '())