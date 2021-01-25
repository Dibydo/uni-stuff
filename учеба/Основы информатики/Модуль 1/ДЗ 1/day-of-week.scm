(define (day-of-week d m y)
  (if (< m 3)
      (list (+ 1 (remainder (- ((+ d 3 y (quotient (- y 1) 4) (quotient (- y 1) 400) (quotient (+ (* 31 m) 10) 12))) (quotient (- y 1) 100)) 7)))
      (list (+ 1 (remainder (- (+ d y (quotient y 4) (quotient y 400) (quotient (+ (* 31 m) 10) 12)) (quotient y 100)) 7)))
)
  )
 