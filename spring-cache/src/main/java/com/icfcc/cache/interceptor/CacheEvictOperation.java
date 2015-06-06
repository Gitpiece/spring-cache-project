package com.icfcc.cache.interceptor;


/**
 * Class describing a cache 'evict' operation.
 *
 * @author Costin Leau
 * @since 3.1
 */
public class CacheEvictOperation extends CacheOperation {

    private boolean cacheWide = false;
    private boolean beforeInvocation = false;

    public void setCacheWide(boolean cacheWide) {
        this.cacheWide = cacheWide;
    }

    public boolean isCacheWide() {
        return this.cacheWide;
    }

    public void setBeforeInvocation(boolean beforeInvocation) {
        this.beforeInvocation = beforeInvocation;
    }

    public boolean isBeforeInvocation() {
        return this.beforeInvocation;
    }

    @Override
    protected StringBuilder getOperationDescription() {
        StringBuilder sb = super.getOperationDescription();
        sb.append(",");
        sb.append(this.cacheWide);
        sb.append(",");
        sb.append(this.beforeInvocation);
        return sb;
    }
}
