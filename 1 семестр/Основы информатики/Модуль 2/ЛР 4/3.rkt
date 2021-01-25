(define memoized-tribonacci
  (let ((memo '()))
    (lambda (n)
      (let ((memoized (assq n memo)))
        (if (not (equal? memoized #f))
            (cadr memoized)
            (let ((new-value
                   (if (<= n 1)
                       0
                       (if (= n 2)
                           1
                           (+ (memoized-tribonacci (- n 3)) (memoized-tribonacci (- n 2)) (memoized-tribonacci (- n 1)))))))
              (set! memo (cons (list n new-value) memo))
              (display memo)
              (newline)
              new-value))))))




;(memoized-tribonacci 1)
;(memoized-tribonacci 2)
;(memoized-tribonacci 3)
;(memoized-tribonacci 4)
;(memoized-tribonacci 5)
;(memoized-tribonacci 6)
;(memoized-tribonacci 7)
;(memoized-tribonacci 8)
;(memoized-tribonacci 9)
;(memoized-tribonacci 10)
