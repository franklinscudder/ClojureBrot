(ns mandelbrot
  (:require [clojure2d.core :as c2d])
  (:require [clojure2d.pixels :as pix])
  (:require [clojure2d.color :as col]))

(def max_iter 30)
(def X 1000)
(def Y 1000)

(def pix (pix/pixels X Y))            ; initialise the pixels
(def pix_idx 0)
(def grad (col/gradient :rainbow2))   ; define the colourmap 

(doseq [y0_i (range Y) x0_i (range X)] 
		(def i 0) ; iterator for this pixel
		(def x0 (float (- (* x0_i (/ 2.47 X)) 2)))    ; scale pixel coords
		(def y0 (float (- (* y0_i (/ 2.24 Y)) 1.12)))
		(def x 0.0)     ; initial conditions for madlebrot iterator
		(def y 0.0)
		(while (and (< i max_iter) (<= (+ (* x x) (* y y)) 4)) ; iterate the transform
			(def xtemp (+ (- (* x x) (* y y)) x0))
			(def y (+ (* 2 x y) y0))
			(def x xtemp)
			(def i (inc i))
		)
		(pix/set-color! pix pix_idx (grad (float (/ i max_iter))))  ; set the pixel value
		;(println x0 y0 i)
		(def pix_idx (inc pix_idx))
		;(println pix_idx)
)

(pix/filter-channels pix/normalize pix)       ; normalize the pixel data

(c2d/with-canvas [canv (c2d/canvas X Y)]
	(pix/set-canvas-pixels! canv pix)               ; set the canvas object to have our pixel data
	(c2d/save canv "result.jpg")
	(def window (c2d/show-window canv "mandlebrot set"))  ; save and draw the image
)
(shutdown-agents) ; Won't exit without this line, not sure why.
