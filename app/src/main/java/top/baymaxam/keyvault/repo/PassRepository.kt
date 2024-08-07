package top.baymaxam.keyvault.repo

/**
 * 密码条目仓库
 * @author John
 * @since 02 7月 2024
 */
class PassRepository {

    private val passDao = LocalDatabase.Instance.passDao()

    private val authDao = LocalDatabase.Instance.authDao()


}