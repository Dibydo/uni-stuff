(define (o . xs)
  (define (loop x xs)
    (if (null? xs)
        x
        ((car xs) (loop x (cdr xs)))))
  (lambda (x) (loop x xs)))

(define (f x) (+ x 2))
(define (g x) (* x 3))
(define (h x) (- x))
    
((o f g h) 1)
((o f g) 1)
((o h) 1)
((o) 1)