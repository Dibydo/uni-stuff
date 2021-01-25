(define (iterate f x n)
  (define (iter count buf list_new)
    (if (= count 0)
        list_new
        (iter (- count 1) (f buf) (append list_new (list buf)))))
  (iter n x '()))

(iterate (lambda (x) (* 2 x)) 1 6)
(iterate (lambda (x) (* 2 x)) 1 1)
(iterate (lambda (x) (* 2 x)) 1 0)