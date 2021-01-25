(define (sq x)(* x x))
(define(discr a b c)(if (>= (- (sq b) (* 4 a c)) 0)
                        (if (= (- (sq b) (* 4 a c)) 0)
                            (list (/ (- b) (* 2 a)))
                            (list (/ (- (- b) (sqrt(- (sq b) (* 4 a c)))) (* 2 a)) (/ (+ (- b) (sqrt(- (sq b) (* 4 a c)))) (* 2 a))))
                        (write '()))
  )



