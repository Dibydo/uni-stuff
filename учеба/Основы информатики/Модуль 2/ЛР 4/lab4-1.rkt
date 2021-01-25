(define call/cc call-with-current-continuation)
(define exit #f)
(define (use-assertions)
  (call/cc (lambda (escape) (set! exit escape))))

(define-syntax assert
  (syntax-rules ()
    ((_ expr) (if (not expr)
                  (begin (display "FAILED :")
                         (display (quote expr))
                         (exit))))))

;------------------------------------------------------------------------------------------

(use-assertions)
(define (1/x x)
  (assert (not (zero? x)))
  (write (/ 1 x))
  (newline))

(map 1/x '(1 2 3 0 5))
#\newline
(map 1/x '(-2 -1 0 1 2))