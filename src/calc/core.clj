(ns calc.core
  (:require
   [clojure.data.json :as json]
   [clojure.java.io   :as io]
   [clojure.string :as str])
  (:gen-class
   :name       calc.core.ApiHandler
   :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

(defn handle-request [handler]
  (fn [_ input-stream output-stream context]
    (with-open [in  (io/reader input-stream)
                out (io/writer output-stream)]
      (let [request (json/read in :key-fn keyword)]
        (-> request
            (handler context)
            (json/write out))))))

(defmulti calc :op)

(defmethod calc :add [ex]
  (+ (:f ex) (:l ex)))
(defmethod calc :minus [ex]
  (- (:f ex) (:l ex)))
(defmethod calc :multiply [ex]
  (* (:f ex) (:l ex)))
(defmethod calc :divid [ex]
  (/ (:f ex) (:l ex)))
(defmethod calc :default [_]
  "Do not know such operation")

(defn oper [op]
  (condp = op
    "+" :add
    "-" :minus
    "/" :divid
    "*" :multiply))

(defn expression-heandler [expr]
  (let [val (str/split expr #" ")]
    (if (= (count val) 3)
      (calc {:op (oper (second val))
             :f    (Double.   (first val))
             :l    (Double.   (last val))})
      (str "Sorry wrong input"))))

(def -handleRequest
  "Lambda"
  (handle-request
   (fn [event context]
     {:status 200
      :body   (expression-heandler
               (:expr
                (json/read-str
                 (:body event)
                 :key-fn keyword)))})))