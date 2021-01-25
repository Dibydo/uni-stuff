(define (s<->s inp)
  (if (symbol? inp)
      (symbol->string inp)
      (string->symbol inp)))
  
(define (strs->sym . strings)
  (s<->s (apply string-append strings)))
  
(define-syntax pred-op
  (syntax-rules ()
    ((_ type sts ...) (begin (eval '(define type (list 'sts ...)) (interaction-environment))
                             (eval (list 'define (list (strs->sym (s<->s 'type) "?") 'x) '(and (list? x) (member (car x) type) (list? x))) (interaction-environment))))))

(define-syntax vec-op
  (syntax-rules ()
    ((_ type args ...) (eval '(define (type args ...) (list 'type args ...)) (interaction-environment)))))

(define-syntax define-data
  (syntax-rules ()
    ((_ type ((st args ...) ...)) (begin
                                    (pred-op type st ...)
                                    (vec-op st args ...) ...))))

(define (matchh1 expr var dict)
  (if (null? expr)
      dict
      (if (symbol? (car expr))
          (if (equal? (car expr) (car var))
              (matchh1 (cdr expr) (cdr var) dict)
              #f)
          (matchh1 (cdr expr) (cdr var) (cons (list (car var) (car expr)) dict)))))

(define (mts var dict)
  (eval `(let ,dict ,var) (interaction-environment)))

(define (matchh2 expr rules)
  (let loop ((i rules))
    (if (null? i)
        #f
        (let ((var (caar i)) (us (cadar i)))
          (let ((res (matchh1 expr var '())))
            (if res
                (mts us res)
                (loop (cdr i))))))))

(define-syntax match
  (syntax-rules ()
    ((_ expr (var us) ...) (matchh2 expr '((var us) ...)))))

(define-data figure ((square a)
                     (rectangle a b)
                     (triangle a b c)
                     (circle r)))

(define s (square 10))
(define r (rectangle 10 20))
(define t (triangle 10 20 30))
(define c (circle 10))

(and (figure? s)
     (figure? r)
     (figure? t)
     (figure? c))