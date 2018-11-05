LibreOffice Türkçe Dil Araçları
==================

LibreOffice Türkçe Dil Araçları, [Zemberek-NLP](https://github.com/ahmetaa/zemberek-nlp) Türkçe Doğal Dil İşleme kütüphanesi kullanılarak yazılmış bir Türkçe Yazım Denetimi eklentisidir.

![](https://github.com/COMU/zemberek-extension/blob/master/site/images/example.gif)

## Gereksinimler

### Java 8 veya üstü

LibreOffice Türkçe Dil Araçları'nı kullanabilmek için sisteminizde Java 8 veya üzeri bir sürümünün kurulu olması gerekmektedir.

Aşağıdaki komutu kullanarak sisteminizde kurulu Java sürümünü öğrenebilirsiniz.

```
$ java -version
```
Eğer aşağıdaki gibi 1.8.0 ya da üstü bir sürüm olduğunu gösteren bir çıktı alırsanız. Sisteminizdeki Java sürümü LibreOffice Türkçe Dil Araçları'nı kullanmaya uygundur.

```
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
```

Eğer sisteminizde Java kurulu değilse ya da daha düşük bir Java sürümü kurulu ise aşağıdaki komutları kullanarak Java 8 kurabilirsiniz.

```
$ sudo apt update
$ sudo apt install openjdk-8-jre
```


# Kurulum

* Zemberek LibreOffice eklentisini kurmak için .oxt uzantılı eklenti dosyasını [buradan]() indirin.

* Araçlar(Tools) menüsünden Eklenti Yöneticisini(Extension Manager) seçerek veya ***Ctrl + Alt + E*** kısayolunu kullanarak Eklenti yöneticisini açın.

![Eklenti Yöneticisi](https://github.com/COMU/zemberek-extension/blob/master/site/images/extension_manager.png)

* Eklenti yöneticisi penceresinde Ekle(Add) düğmesine tıklayın.

![Eklenti Ekle](https://github.com/COMU/zemberek-extension/blob/master/site/images/add_extension.png)

* Açılan dosya seçimi penceresinde daha önce indirdiğiniz eklenti dosyasını seçin ve ardından Aç(Open) düğmesine basın.

![Eklenti Dosyası Seçimi](https://github.com/COMU/zemberek-extension/blob/master/site/images/select_extension.png)

* Eklentinin yüklenmesi birkaç saniye alabilir. Eklenti yüklendikten sonra Eklenti yöneticisini kapatabilirsiniz.

![Kapat](https://github.com/COMU/zemberek-extension/blob/master/site/images/close_em.png)

* Eklenti yüklendikten sonra kullanmaya başlamak için LibreOffice yeniden başlatılmalıdır. Bunun için Eklenti yöneticisi kapatıldıktan sonra gelen uyarı kutusunda Şimdi Yeniden Başlat(Restart Now) düğmesine tıklayarak LibreOffice'i yeniden başlatabilirsiniz.

![Yeniden Başlat](https://github.com/COMU/zemberek-extension/blob/master/site/images/restart.png)

## Olası Sorunlar
* LibreOffice belgesi düzenlemeye başlandığı esnada Zemberek Eklentisinin başlatılması biraz zaman alabilir. Bu birkaç saniye donmaya sebep olabilir.
* Bu bir erken deneme sürümü olduğu için beklenmedik hatalar ile karşılaşılması, yazım denetimi sırasında bazı yanlışlıkların ortaya çıkması olasıdır. Böyle durumlarda lütfen bizi bilgilendiriniz.

Bu konuda daha fazla bilgi almak isterseniz [Issues](https://github.com/COMU/zemberek-extension/issues) bölümüne bakabilirsiniz. Yeni bir sorunla karşılaşırsanız yeni bir issue oluşturarak bizi bilgilendirebilirsiniz.

## LİSANS
Bu projenin içeriği MPLv2 ile lisanslanmıştır. Detaylı bilgi için [Mozilla Public License 2.0](https://github.com/COMU/zemberek-extension/blob/master/LICENSE) inceleyebilirsiniz.

## Sorumluluk Reddi Beyanı
LibreOffice Türkçe Dil Araçları, belge hazırlama sırasında yapılan yazım ve imla hatalarını en aza indirmek amacıyla hazırlanmış bir Türkçe Yazım Denetimi yazılımıdır. Bu yazılımın kurulumu veya kullanımı sırasında; amacı dışında, kötü bir niyetle ya da verilen talimatların dışında kullanılması durumunda oluşabilecek herhangi bir hasar veya sorundan; kullanıldığı bölgenin yasalarına aykırı şekilde kullanılması durumunda ortaya çıkabilecek herhangi bir yasal işlemden kullanıcının kendisi sorumludur.
