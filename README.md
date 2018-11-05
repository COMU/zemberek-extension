Zemberek-Extension
==================

Zemberek-Extension, [Zemberek-NLP](https://github.com/ahmetaa/zemberek-nlp) Türkçe Doğal Dil İşleme kütüphanesi kullanılarak yazılmış bir Türkçe Yazım Denetimi eklentisidir.

![](https://github.com/COMU/zemberek-extension/blob/master/site/images/example.gif)

## Gereksinimler

### Java 8 veya üstü

Zemberek-Extension kullanabilmek için sisteminizde Java 8 veya üzeri bir sürümünün kurulu olması gerekmektedir.

Aşağıdaki komutu kullanarak sisteminizde kurulu Java sürümünü öğrenebilirsiniz.

```
$ java --version
```


Eğer sisteminizde Java kurulu değilse ya da daha düşük bir Java sürümü kurulu ise aşağıdaki komutları kullanarak Java 8 kurabilirsiniz.

```
$ sudo apt-get update
$ sudo apt-get install openjdk-8-jdk
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

Bu konuda daha fazla bilgi almak isterseniz [Issues](https://github.com/COMU/zemberek-extension/issues) bölümüne bakabilirsiniz. Yeni bir sorunla karşılaşırsanız yeni bir issue oluşturarak bizi bilgilendirebilirsiniz.


### Sorumluluk Reddi Beyanı
Zemberek-Extension, belge hazırlama sırasında yapılan yazım ve imla hatalarını en aza indirmek amacıyla hazırlanmış bir Türkçe Yazım Denetimi yazılımıdır. Bu yazılımın kurulumu veya kullanımı sırasında; amacı dışında, kötü bir niyetle ya da verilen talimatların dışında kullanılması durumunda oluşabilecek herhangi bir hasar veya sorundan; kullanıldığı bölgenin yasalarına aykırı şekilde kullanılması durumunda ortaya çıkabilecek herhangi bir yasal işlemdan kullanıcının kendisi sorumludur.
