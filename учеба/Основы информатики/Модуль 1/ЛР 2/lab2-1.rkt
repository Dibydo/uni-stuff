(define (count x xs)
  (cond
    ((null? xs) 0)
    ((equal? x (car xs)) (+ 1 (count x (cdr xs))))
    (else (count x (cdr xs)))))

(count 'a '(a b c a))
(count 'b '(a c d))
(count 'a '())