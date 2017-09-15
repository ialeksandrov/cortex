(ns cortex.loss-test
  (:require [clojure.test :refer :all]
            [cortex.loss.core :as loss]
            [cortex.loss.center :as center-loss]
            [cortex.keyword-fn :as keyword-fn]
            [cortex.loss.yolo2 :as yolo]))


(deftest labes->indexes
  (let [labels [[1 0 0 0]
                [0 1 0 0]
                [0 0 1 0]
                [0 0 0 1]]
        augment-fn (get (center-loss/labels->indexes-augmentation :stream)
                        :augmentation)]
    (is (= [0 1 2 3]
           (keyword-fn/call-keyword-fn augment-fn labels)))))


(deftest labes->inverse-counts
  (let [labels [[1 0 0 0 0]
                [0 0 1 0 0]
                [1 0 0 0 0]
                [0 0 0 1 0]]
        augment-fn (get (center-loss/labels->inverse-counts-augmentation :stream)
                        :augmentation)]
    (is (= [0.5 1.0 0.5 1.0]
           (keyword-fn/call-keyword-fn augment-fn labels)))))


(deftest mse-loss-not-nan
  (let [mse-loss (loss/mse-loss)
        buffer-map {:output 0.0
                    :labels 0.2}
        loss-value (loss/loss mse-loss buffer-map)]
    (is (not (Double/isNaN loss-value)))))

(defonce prediction [3.4018149375915527 4.931896686553955 -2.516183376312256 -2.528874635696411 -5.036355018615723 -14.299439430236816 -12.540074348449707 -13.247929573059082 -7.656569004058838 -13.79504108428955 -12.445066452026367 -16.211172103881836 -11.071887016296387 -14.22611141204834 -12.352143287658691 0.4893481731414795 3.0628178119659424 -2.0166049003601074 -1.7760202884674072 -6.209695816040039 -12.893905639648438 -11.215829849243164 -11.04458236694336 -7.535778522491455 -12.237252235412598 -10.662593841552734 -12.932779312133789 -10.246973991394043 -12.570954322814941 -12.50193977355957 1.1219536066055298 -0.2603178918361664 -3.0388550758361816 -3.0607638359069824 9.577865600585938 -12.787059783935547 -12.275617599487305 -11.723101615905762 -3.895458936691284 -14.694849014282227 -9.482973098754883 -13.50570297241211 -12.876662254333496 -8.488009452819824 -11.271052360534668 0.6401327252388 -1.530005693435669 -2.046356678009033 -2.0846710205078125 9.848857879638672 -12.526190757751465 -12.73081111907959 -10.287206649780273 -2.5662872791290283 -15.440924644470215 -10.682437896728516 -13.800149917602539 -11.401325225830078 -8.283161163330078 -13.448307991027832 -0.3344091475009918 -1.749505639076233 -2.696699619293213 -2.623868942260742 -6.740039348602295 -11.479842185974121 -11.612767219543457 -8.773683547973633 -7.811320781707764 -11.141190528869629 -10.81135368347168 -12.383349418640137 -9.801340103149414 -9.075427055358887 -10.271787643432617 0.39907872676849365 -4.456862926483154 -1.3485329151153564 -1.3252604007720947 -5.410799026489258 -13.159939765930176 -11.172205924987793 -12.476130485534668 -6.035907745361328 -12.866413116455078 -10.978667259216309 -11.767145156860352 -12.00728988647461 -11.084220886230469 -10.376641273498535 0.36138156056404114 -1.942919373512268 -2.7630486488342285 -3.0264883041381836 -5.55479097366333 -12.094675064086914 -11.234525680541992 -9.53056526184082 -9.935418128967285 -13.48343563079834 -11.564245223999023 -13.911650657653809 -13.423816680908203 -9.993945121765137 -9.69603157043457 -0.544330894947052 -2.0330660343170166 -1.8825409412384033 -1.6777873039245605 -5.0330586433410645 -14.347651481628418 -10.42786979675293 -10.719660758972168 -10.61976146697998 -12.554346084594727 -11.301957130432129 -12.279189109802246 -10.688892364501953 -12.661337852478027 -9.245044708251953 -2.762155771255493 1.4543710947036743 -2.626371383666992 -2.5607147216796875 3.7029974460601807 -13.862357139587402 -9.221968650817871 -11.799028396606445 -11.87386703491211 -10.775388717651367 -14.674111366271973 -14.968389511108398 -4.1108245849609375 -16.223054885864258 -11.250848770141602 -3.2301907539367676 3.0791971683502197 -2.053764820098877 -2.091071367263794 3.4438180923461914 -12.467081069946289 -8.663948059082031 -12.521342277526855 -9.541818618774414 -10.949180603027344 -14.244463920593262 -14.762375831604004 -2.893550395965576 -16.530982971191406 -11.799184799194336 -0.08584046363830566 -0.6571543216705322 -1.9384119510650635 -1.9593887329101562 -3.6379144191741943 -12.02888298034668 -5.812874794006348 -10.703278541564941 -11.828027725219727 -13.66573429107666 -10.938318252563477 -10.718253135681152 -9.059270858764648 -13.203070640563965 -11.486933708190918 -4.092936038970947 -2.750041961669922 -1.8173261880874634 -1.7672901153564453 -2.952911615371704 -10.831649780273438 -5.310796737670898 -10.666059494018555 -9.720937728881836 -11.402220726013184 -12.041367530822754 -10.365105628967285 -6.899791240692139 -11.684395790100098 -14.599628448486328 2.0560696125030518 -1.4461438655853271 -2.3864972591400146 -2.3532731533050537 -8.28315258026123 -9.560935020446777 -5.262597560882568 -9.780882835388184 -8.433259010314941 -8.945474624633789 -12.214554786682129 -9.286075592041016 -10.452247619628906 -8.794573783874512 -10.137259483337402 -1.2374303340911865 -4.133093357086182 -1.9145853519439697 -1.874255895614624 -8.375925064086914 -12.272165298461914 -7.051066875457764 -10.953269004821777 -9.890045166015625 -10.9230318069458 -11.471144676208496 -9.656288146972656 -9.321678161621094 -11.007227897644043 -9.211254119873047 1.364958643913269 -1.4051733016967773 -1.8019819259643555 -1.9217101335525513 -10.373394012451172 -11.229580879211426 -9.505952835083008 -9.457395553588867 -7.533073425292969 -10.746406555175781 -11.14565658569336 -12.188965797424316 -9.291519165039062 -9.371975898742676 -10.543046951293945 -0.8726199865341187 -1.37063729763031 -1.693695068359375 -1.4742536544799805 -11.307428359985352 -12.371173858642578 -9.731006622314453 -9.954019546508789 -8.774961471557617 -12.364394187927246 -11.167474746704102 -9.645256042480469 -10.534924507141113 -9.648096084594727 -9.230208396911621 -1.5514193773269653 -0.09997241199016571 -2.698646068572998 -2.657029390335083 -9.761283874511719 -9.264084815979004 -9.602577209472656 -12.153712272644043 -9.520421028137207 -10.782974243164062 -11.788907051086426 -10.809574127197266 -9.083586692810059 -10.912665367126465 -10.419197082519531 0.5169843435287476 0.7081140875816345 -1.9200353622436523 -1.816813588142395 -7.191432476043701 -9.585676193237305 -9.118585586547852 -11.095365524291992 -10.90502643585205 -10.575332641601562 -10.504759788513184 -10.493290901184082 -8.98007583618164 -10.797525405883789 -11.457634925842285 -2.021932601928711 -0.4770965278148651 -2.552745819091797 -2.5367431640625 -10.21998119354248 -11.03311824798584 -8.11099624633789 -7.811604976654053 -11.446349143981934 -9.007136344909668 -8.92706298828125 -9.542898178100586 -9.9558744430542 -10.650355339050293 -8.864261627197266 -0.20677566528320312 0.15262974798679352 -1.6398861408233643 -1.686181902885437 -10.080955505371094 -11.100530624389648 -8.110663414001465 -11.405377388000488 -10.04511833190918 -12.128704071044922 -8.111451148986816 -11.157063484191895 -9.935064315795898 -9.230006217956543 -10.861745834350586 -0.9045580625534058 1.099794626235962 -2.553436756134033 -2.548269033432007 3.586038112640381 -2.7360730171203613 -8.574201583862305 -9.0950927734375 -10.875539779663086 -8.622364044189453 -11.18211555480957 -8.3191499710083 -10.413030624389648 -9.24936580657959 -9.507513999938965 -1.0142875909805298 0.0071748606860637665 -1.9186570644378662 -1.8855199813842773 3.235830307006836 -3.609795331954956 -10.392461776733398 -12.115955352783203 -12.09514331817627 -10.035623550415039 -11.717949867248535 -11.951744079589844 -10.166823387145996 -8.71694278717041 -11.130901336669922 0.25909000635147095 -2.438568353652954 -2.3339192867279053 -2.4789528846740723 -7.743309020996094 -8.792551040649414 -11.31109619140625 -10.109607696533203 -10.100207328796387 -10.294139862060547 -9.460134506225586 -8.18071460723877 -10.264902114868164 -9.590932846069336 -11.21860122680664 0.4446427822113037 -2.430732250213623 -1.5777806043624878 -1.5439330339431763 -6.415444850921631 -9.155023574829102 -11.053192138671875 -10.55257797241211 -10.008082389831543 -9.756380081176758 -9.12864875793457 -9.42170524597168 -10.776114463806152 -9.468585968017578 -9.665189743041992 -1.2466931343078613 0.27457094192504883 -1.9110674858093262 -2.08182430267334 -10.011916160583496 -10.599766731262207 -10.540891647338867 -11.146904945373535 -9.9702787399292 -10.957662582397461 -10.775788307189941 -9.815844535827637 -11.070836067199707 -10.095297813415527 -12.628643035888672 -2.2849607467651367 -0.08426190912723541 -2.2568235397338867 -1.8960299491882324 -10.374486923217773 -11.446054458618164 -11.503807067871094 -10.776640892028809 -12.317302703857422 -11.441167831420898 -12.713543891906738 -13.00162410736084 -10.900346755981445 -11.03012466430664 -10.661999702453613 -1.1644980907440186 -2.527587890625 -2.648899555206299 -2.469493865966797 -11.67032241821289 -10.5244779586792 -9.915546417236328 -11.431266784667969 -10.040023803710938 -8.7212553024292 -11.822643280029297 -12.712639808654785 -7.54681396484375 -10.41727066040039 -8.9129638671875 -1.9435920715332031 0.918323814868927 -1.9231503009796143 -2.1024351119995117 -10.299568176269531 -11.25082015991211 -10.278526306152344 -10.970381736755371 -11.02756404876709 -10.47722339630127 -11.375020980834961 -10.809561729431152 -9.964285850524902 -10.842744827270508 -9.927066802978516 -3.4660394191741943 0.31558412313461304 -2.5053911209106445 -2.7070353031158447 -7.705524444580078 -7.992099285125732 -9.73829174041748 -9.82773494720459 -10.177939414978027 -8.996139526367188 -10.495935440063477 -8.515789031982422 -8.734939575195312 -8.854262351989746 -10.514082908630371 -2.5785162448883057 -1.3238208293914795 -1.7243733406066895 -1.750445008277893 -9.017760276794434 -11.237763404846191 -11.055588722229004 -11.352035522460938 -12.170188903808594 -9.136083602905273 -10.582801818847656 -12.706414222717285 -10.5836181640625 -11.423632621765137 -10.572844505310059 -1.7338337898254395 -2.796721935272217 -2.7698309421539307 -2.685915231704712 -9.502788543701172 -9.541107177734375 -10.481982231140137 -10.309396743774414 -12.670015335083008 -12.895472526550293 -9.229181289672852 -11.634031295776367 -10.354533195495605 -10.887700080871582 -10.857619285583496 -1.6084725856781006 -0.6214513182640076 -1.4793415069580078 -1.304208517074585 -9.390750885009766 -12.443748474121094 -11.931081771850586 -11.622164726257324 -12.39014720916748 -11.439226150512695 -10.57151985168457 -12.083426475524902 -10.296256065368652 -12.6943359375 -11.312227249145508])

(deftest test-yolo-nms
  "This prediction is a 4x4x2x(4 + 1 + 10)-dim'l vector.  For each of the 4x4 grid cells, each of the 2 anchor shapes
  it is a prediction of the x-y-w-h bounding box, the probability of an object being within the box, and the 10-dim'l conditional class probability vector
  for the the mnist digits 3,7,0 spread at random on a grid. Non-maximal suppression reduces the result to just three bounding boxes as desired."
  (let [[grid-x grid-y anchors probability-threshold iou-threshold classes] [4 4 [[2 2] [1 1]] 0.2 0.2 ["0" "1" "2" "3" "4" "5" "6" "7" "8" "9"]]]
    (is (= (yolo/yolo-nms prediction grid-x grid-y anchors probability-threshold iou-threshold classes)
           [{:box-probability 0.9999471953174072,
             :class-probabilities [6.919782044387174E-5
                                   0.0031029308413165774
                                   6.554311419401082E-5
                                   0.0012897862898179075
                                   3.157244478096036E-4
                                   1.1699972776484244E-5
                                   6.970413885462601E-6
                                   0.9950020133763191
                                   1.1889463139359777E-6
                                   1.3494477712308343E-4],
             :bounding-box [0.07383319902345714 0.20634067615426166 0.25355853233235875 0.3826557502308344],
             :class "7",
             :class-probability 0.9949494726108236}
            {:box-probability 0.9690462472084166,
             :class-probabilities [4.7055054730973154E-5
                                   3.83478296161835E-5
                                   4.4155491778792274E-4
                                   0.9957203978045087
                                   2.5512524787372573E-6
                                   2.973979540243035E-4
                                   1.3162332259835077E-5
                                   1.4492028394671056E-4
                                   0.0032758998964245883
                                   1.8712674221963785E-5],
             :bounding-box [0.16998089166251254 0.15113074293324735 0.34904174097596385 0.3268824800723231],
             :class "3",
             :class-probability 0.9648991147613308}
            {:box-probability 0.9730391397325113,
             :class-probabilities [0.9854322172016938
                                   0.0028718366630610674
                                   0.0017058449977166846
                                   2.8754197815617145E-4
                                   0.002736799897006186
                                   2.1162018451882278E-4
                                   0.003706186277010858
                                   4.566320917677239E-4
                                   0.001461973966881119
                                   0.0011293467421876226],
             :bounding-box [0.47340453189126197 0.5886760980733168 0.6706528258654724 0.7864347133066463],
             :class "0",
             :class-probability 0.9588641168906374}]))))

(deftest test-yolo-nonoverlapping-nms
  (let [[grid-x grid-y anchors probability-threshold iou-threshold classes]
        [4 4 [[2 2] [1 1]] 0.2 0.2 ["0" "1" "2" "3" "4" "5" "6" "7" "8" "9"]]]
    (is (= (yolo/yolo-non-overlapping-nms prediction grid-x grid-y anchors probability-threshold iou-threshold classes)
           {:certainty-threshold       0.2,
            :iou-suppression-threshold 0.2,
            :all-boxes                 [{:bounding-box-xywh [0.18858772764517892 0.35882139123717527 0.1547412165184782 0.15305537296923682],
                                         :bounding-box      [0.11121711938593982 0.2822937047525569 0.26595833590441803 0.43534907772179365],
                                         :class             "7",
                                         :class-probability 0.9909238652831338}
                                        {:bounding-box-xywh [0.16369586567790795 0.29449821319254804 0.1797253333089016 0.17631507407657274],
                                         :bounding-box      [0.07383319902345714 0.20634067615426166 0.25355853233235875 0.3826557502308344],
                                         :class             "7",
                                         :class-probability 0.9949494726108236}
                                        {:bounding-box-xywh [0.2648509494536324 0.2026675595793083 0.190184755464211 0.1965318152479854],
                                         :bounding-box      [0.16975857172152692 0.10440165195531559 0.3599433271857379 0.300933467203301],
                                         :class             "3",
                                         :class-probability 0.961069599318107}
                                        {:bounding-box-xywh [0.2595113163192382 0.23900661150278524 0.1790608493134513 0.17575173713907574],
                                         :bounding-box      [0.16998089166251254 0.15113074293324735 0.34904174097596385 0.3268824800723231],
                                         :class             "3",
                                         :class-probability 0.9648991147613308}
                                        {:bounding-box-xywh [0.5720286788783672 0.6875554056899815 0.19724829397421043 0.19775861523332955],
                                         :bounding-box      [0.47340453189126197 0.5886760980733168 0.6706528258654724 0.7864347133066463],
                                         :class             "0",
                                         :class-probability 0.9588641168906374}
                                        {:bounding-box-xywh [0.5665354002695809 0.6254484268691807 0.19157503627411485 0.19477559630713293],
                                         :bounding-box      [0.4707478821325235 0.5280606287156142 0.6623229184066384 0.7228362250227471],
                                         :class             "0",
                                         :class-probability 0.9510189576843188}],
            :suppressed-boxes          [{:bounding-box-xywh [0.16369586567790795 0.29449821319254804 0.1797253333089016 0.17631507407657274],
                                         :bounding-box      [0.07383319902345714 0.20634067615426166 0.25355853233235875 0.3826557502308344],
                                         :class             "7",
                                         :class-probability 0.9949494726108236}
                                        {:bounding-box-xywh [0.2595113163192382 0.23900661150278524 0.1790608493134513 0.17575173713907574],
                                         :bounding-box      [0.16998089166251254 0.15113074293324735 0.34904174097596385 0.3268824800723231],
                                         :class             "3",
                                         :class-probability 0.9648991147613308}
                                        {:bounding-box-xywh [0.5720286788783672 0.6875554056899815 0.19724829397421043 0.19775861523332955],
                                         :bounding-box      [0.47340453189126197 0.5886760980733168 0.6706528258654724 0.7864347133066463],
                                         :class             "0",
                                         :class-probability 0.9588641168906374}]}))))